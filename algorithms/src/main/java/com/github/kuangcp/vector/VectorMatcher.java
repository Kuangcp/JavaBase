package com.github.kuangcp.vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 向量匹配服务
 * 用于专业术语的向量化和相似度匹配
 */
public class VectorMatcher {
    
    private final Vectorizer vectorizer;
    private final Map<String, double[]> termVectors;
    private final List<String> terms;
    private boolean isInitialized = false;
    
    public VectorMatcher(Vectorizer.VectorizationStrategy strategy) {
        this.vectorizer = new Vectorizer(strategy);
        this.termVectors = new ConcurrentHashMap<>();
        this.terms = new ArrayList<>();
    }
    
    /**
     * 初始化匹配器，添加专业术语并构建向量
     * @param terms 专业术语列表
     */
    public void initialize(List<String> terms) {
        if (terms == null || terms.isEmpty()) {
            throw new IllegalArgumentException("术语列表不能为空");
        }
        
        this.terms.clear();
        this.terms.addAll(terms);
        this.termVectors.clear();
        
        // 构建词汇表
        vectorizer.buildVocabulary(terms);
        
        // 为每个术语生成向量
        for (String term : terms) {
            double[] vector = vectorizer.vectorize(term);
            termVectors.put(term, vector);
        }
        
        isInitialized = true;
    }
    
    /**
     * 添加单个术语
     * @param term 术语
     */
    public void addTerm(String term) {
        if (term == null || term.trim().isEmpty()) {
            return;
        }
        
        if (!isInitialized) {
            // 如果还没初始化，先初始化
            initialize(Arrays.asList(term));
            return;
        }
        
        if (!terms.contains(term)) {
            terms.add(term);
            double[] vector = vectorizer.vectorize(term);
            termVectors.put(term, vector);
        }
    }
    
    /**
     * 查找与输入字符串最相似的术语
     * @param input 输入字符串
     * @param topK 返回前K个最相似的结果
     * @param minSimilarity 最小相似度阈值
     * @return 匹配结果列表，按相似度降序排列
     */
    public List<MatchResult> findSimilarTerms(String input, int topK, double minSimilarity) {
        if (!isInitialized) {
            throw new IllegalStateException("匹配器尚未初始化，请先调用initialize方法");
        }
        
        if (input == null || input.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        // 将输入字符串向量化
        double[] inputVector = vectorizer.vectorize(input);
        
        // 计算与所有术语的相似度
        List<MatchResult> results = new ArrayList<>();
        
        for (String term : terms) {
            double[] termVector = termVectors.get(term);
            if (termVector != null) {
                double similarity = SimilarityCalculator.cosineSimilarity(inputVector, termVector);
                
                if (similarity >= minSimilarity) {
                    results.add(new MatchResult(term, similarity, termVector));
                }
            }
        }
        
        // 按相似度排序并返回前K个结果
        return results.stream()
                .sorted()
                .limit(topK)
                .collect(Collectors.toList());
    }
    
    /**
     * 查找与输入字符串最相似的术语（使用默认参数）
     * @param input 输入字符串
     * @return 匹配结果列表
     */
    public List<MatchResult> findSimilarTerms(String input) {
        return findSimilarTerms(input, 10, 0.1);
    }
    
    /**
     * 查找与输入字符串最相似的术语（指定数量）
     * @param input 输入字符串
     * @param topK 返回前K个最相似的结果
     * @return 匹配结果列表
     */
    public List<MatchResult> findSimilarTerms(String input, int topK) {
        return findSimilarTerms(input, topK, 0.1);
    }
    
    /**
     * 批量查找相似术语
     * @param inputs 输入字符串列表
     * @param topK 每个输入返回前K个最相似的结果
     * @param minSimilarity 最小相似度阈值
     * @return 每个输入对应的匹配结果
     */
    public Map<String, List<MatchResult>> batchFindSimilarTerms(List<String> inputs, int topK, double minSimilarity) {
        Map<String, List<MatchResult>> results = new HashMap<>();
        
        for (String input : inputs) {
            List<MatchResult> matches = findSimilarTerms(input, topK, minSimilarity);
            results.put(input, matches);
        }
        
        return results;
    }
    
    /**
     * 获取术语的向量表示
     * @param term 术语
     * @return 向量，如果术语不存在则返回null
     */
    public double[] getTermVector(String term) {
        return termVectors.get(term);
    }
    
    /**
     * 获取所有术语的向量表示
     * @return 术语到向量的映射
     */
    public Map<String, double[]> getAllTermVectors() {
        return new HashMap<>(termVectors);
    }
    
    /**
     * 获取所有术语列表
     * @return 术语列表
     */
    public List<String> getAllTerms() {
        return new ArrayList<>(terms);
    }
    
    /**
     * 获取向量维度
     * @return 向量维度
     */
    public int getVectorDimension() {
        return vectorizer.getVectorDimension();
    }
    
    /**
     * 获取词汇表
     * @return 词汇表
     */
    public Map<String, Integer> getVocabulary() {
        return vectorizer.getVocabulary();
    }
    
    /**
     * 检查是否已初始化
     * @return 是否已初始化
     */
    public boolean isInitialized() {
        return isInitialized;
    }
    
    /**
     * 获取术语数量
     * @return 术语数量
     */
    public int getTermCount() {
        return terms.size();
    }
    
    /**
     * 清除所有数据
     */
    public void clear() {
        terms.clear();
        termVectors.clear();
        isInitialized = false;
    }
    
    /**
     * 移除指定术语
     * @param term 要移除的术语
     * @return 是否成功移除
     */
    public boolean removeTerm(String term) {
        boolean removed = terms.remove(term);
        if (removed) {
            termVectors.remove(term);
        }
        return removed;
    }
    
    /**
     * 更新术语的向量（当词汇表发生变化时）
     */
    public void updateVectors() {
        if (!isInitialized) {
            return;
        }
        
        termVectors.clear();
        for (String term : terms) {
            double[] vector = vectorizer.vectorize(term);
            termVectors.put(term, vector);
        }
    }
} 
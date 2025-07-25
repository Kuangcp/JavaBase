package com.github.kuangcp.vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 高级向量匹配器
 * 支持多种匹配策略、缓存优化、并行计算等
 */
public class AdvancedVectorMatcher {
    
    private final VectorMatcher[] matchers;
    private final Map<String, double[]> cachedVectors;
    private final List<String> terms;
    private final MatchingStrategy strategy;
    private boolean isInitialized = false;
    
    public AdvancedVectorMatcher(MatchingStrategy strategy) {
        this.strategy = strategy;
        this.matchers = new VectorMatcher[Vectorizer.VectorizationStrategy.values().length];
        this.cachedVectors = new ConcurrentHashMap<>();
        this.terms = new ArrayList<>();
        
        // 初始化所有向量化策略的匹配器
        int i = 0;
        for (Vectorizer.VectorizationStrategy vs : Vectorizer.VectorizationStrategy.values()) {
            matchers[i++] = new VectorMatcher(vs);
        }
    }
    
    /**
     * 初始化匹配器
     * @param terms 专业术语列表
     */
    public void initialize(List<String> terms) {
        if (terms == null || terms.isEmpty()) {
            throw new IllegalArgumentException("术语列表不能为空");
        }
        
        this.terms.clear();
        this.terms.addAll(terms);
        this.cachedVectors.clear();
        
        // 初始化所有匹配器
        for (VectorMatcher matcher : matchers) {
            matcher.initialize(terms);
        }
        
        isInitialized = true;
    }
    
    /**
     * 查找相似术语（使用多种策略）
     * @param input 输入字符串
     * @param topK 返回前K个结果
     * @param minSimilarity 最小相似度阈值
     * @return 匹配结果列表
     */
    public List<MatchResult> findSimilarTerms(String input, int topK, double minSimilarity) {
        if (!isInitialized) {
            throw new IllegalStateException("匹配器尚未初始化");
        }
        
        if (input == null || input.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        switch (strategy) {
            case ENSEMBLE:
                return findSimilarTermsEnsemble(input, topK, minSimilarity);
            case BEST_STRATEGY:
                return findSimilarTermsBestStrategy(input, topK, minSimilarity);
            case WEIGHTED_ENSEMBLE:
                return findSimilarTermsWeightedEnsemble(input, topK, minSimilarity);
            default:
                return matchers[0].findSimilarTerms(input, topK, minSimilarity);
        }
    }
    
    /**
     * 集成学习策略：综合多种向量化方法的结果
     */
    private List<MatchResult> findSimilarTermsEnsemble(String input, int topK, double minSimilarity) {
        Map<String, List<Double>> termScores = new HashMap<>();
        
        // 收集所有匹配器的结果
        for (VectorMatcher matcher : matchers) {
            List<MatchResult> results = matcher.findSimilarTerms(input, topK * 2, minSimilarity);
            for (MatchResult result : results) {
                termScores.computeIfAbsent(result.getTerm(), k -> new ArrayList<>())
                         .add(result.getSimilarity());
            }
        }
        
        // 计算平均分数
        List<MatchResult> ensembleResults = new ArrayList<>();
        for (Map.Entry<String, List<Double>> entry : termScores.entrySet()) {
            double avgScore = entry.getValue().stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
            
            if (avgScore >= minSimilarity) {
                ensembleResults.add(new MatchResult(entry.getKey(), avgScore));
            }
        }
        
        return ensembleResults.stream()
                .sorted()
                .limit(topK)
                .collect(Collectors.toList());
    }
    
    /**
     * 最佳策略：选择表现最好的向量化方法
     */
    private List<MatchResult> findSimilarTermsBestStrategy(String input, int topK, double minSimilarity) {
        List<MatchResult> bestResults = Collections.emptyList();
        double bestScore = 0.0;
        
        for (VectorMatcher matcher : matchers) {
            List<MatchResult> results = matcher.findSimilarTerms(input, topK, minSimilarity);
            if (!results.isEmpty()) {
                double avgScore = results.stream()
                        .mapToDouble(MatchResult::getSimilarity)
                        .average()
                        .orElse(0.0);
                
                if (avgScore > bestScore) {
                    bestScore = avgScore;
                    bestResults = results;
                }
            }
        }
        
        return bestResults;
    }
    
    /**
     * 加权集成：根据策略的权重综合结果
     */
    private List<MatchResult> findSimilarTermsWeightedEnsemble(String input, int topK, double minSimilarity) {
        Map<String, Double> termScores = new HashMap<>();
        double[] weights = {0.3, 0.2, 0.3, 0.2}; // TF-IDF, CharFreq, WordFreq, SimpleHash
        
        // 收集所有匹配器的结果并加权
        for (int i = 0; i < matchers.length; i++) {
            List<MatchResult> results = matchers[i].findSimilarTerms(input, topK * 2, minSimilarity);
            for (MatchResult result : results) {
                termScores.merge(result.getTerm(), 
                               result.getSimilarity() * weights[i], 
                               Double::sum);
            }
        }
        
        // 转换为结果列表
        List<MatchResult> weightedResults = termScores.entrySet().stream()
                .filter(entry -> entry.getValue() >= minSimilarity)
                .map(entry -> new MatchResult(entry.getKey(), entry.getValue()))
                .sorted()
                .limit(topK)
                .collect(Collectors.toList());
        
        return weightedResults;
    }
    
    /**
     * 批量查找相似术语
     * @param inputs 输入字符串列表
     * @param topK 每个输入返回前K个结果
     * @param minSimilarity 最小相似度阈值
     * @return 每个输入对应的匹配结果
     */
    public Map<String, List<MatchResult>> batchFindSimilarTerms(List<String> inputs, int topK, double minSimilarity) {
        Map<String, List<MatchResult>> results = new HashMap<>();
        
        // 并行处理批量查询
        inputs.parallelStream().forEach(input -> {
            List<MatchResult> matches = findSimilarTerms(input, topK, minSimilarity);
            synchronized (results) {
                results.put(input, matches);
            }
        });
        
        return results;
    }
    
    /**
     * 获取指定策略的匹配器
     * @param strategy 向量化策略
     * @return 对应的匹配器
     */
    public VectorMatcher getMatcher(Vectorizer.VectorizationStrategy strategy) {
        int index = strategy.ordinal();
        return matchers[index];
    }
    
    /**
     * 获取所有术语
     * @return 术语列表
     */
    public List<String> getAllTerms() {
        return new ArrayList<>(terms);
    }
    
    /**
     * 获取术语数量
     * @return 术语数量
     */
    public int getTermCount() {
        return terms.size();
    }
    
    /**
     * 检查是否已初始化
     * @return 是否已初始化
     */
    public boolean isInitialized() {
        return isInitialized;
    }
    
    /**
     * 清除所有数据
     */
    public void clear() {
        terms.clear();
        cachedVectors.clear();
        for (VectorMatcher matcher : matchers) {
            matcher.clear();
        }
        isInitialized = false;
    }
    
    /**
     * 获取匹配策略
     * @return 当前使用的匹配策略
     */
    public MatchingStrategy getStrategy() {
        return strategy;
    }
    
    /**
     * 匹配策略枚举
     */
    public enum MatchingStrategy {
        ENSEMBLE,           // 集成学习
        BEST_STRATEGY,      // 最佳策略
        WEIGHTED_ENSEMBLE   // 加权集成
    }
} 
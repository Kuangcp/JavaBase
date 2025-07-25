package com.github.kuangcp.vector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 文本向量化工具类
 * 支持多种向量化策略：TF-IDF、字符频率、词频等
 */
public class Vectorizer {
    
    private final Map<String, Integer> vocabulary;
    private final int vectorDimension;
    private final VectorizationStrategy strategy;
    
    public Vectorizer(VectorizationStrategy strategy) {
        this.strategy = strategy;
        this.vocabulary = new HashMap<>();
        this.vectorDimension = 0;
    }
    
    public Vectorizer(Map<String, Integer> vocabulary, VectorizationStrategy strategy) {
        this.vocabulary = new HashMap<>(vocabulary);
        this.strategy = strategy;
        this.vectorDimension = vocabulary.size();
    }
    
    /**
     * 构建词汇表
     */
    public void buildVocabulary(List<String> documents) {
        Map<String, Integer> wordFreq = new HashMap<>();
        
        for (String doc : documents) {
            Set<String> words = extractWords(doc);
            for (String word : words) {
                wordFreq.merge(word, 1, Integer::sum);
            }
        }
        
        // 按频率排序，取前N个词作为词汇表
        List<Map.Entry<String, Integer>> sortedWords = wordFreq.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());
        
        vocabulary.clear();
        for (int i = 0; i < Math.min(sortedWords.size(), 1000); i++) {
            vocabulary.put(sortedWords.get(i).getKey(), i);
        }
    }
    
    /**
     * 将文本向量化
     */
    public double[] vectorize(String text) {
        switch (strategy) {
            case TF_IDF:
                return vectorizeTFIDF(text);
            case CHARACTER_FREQUENCY:
                return vectorizeCharacterFrequency(text);
            case WORD_FREQUENCY:
                return vectorizeWordFrequency(text);
            case SIMPLE_HASH:
                return vectorizeSimpleHash(text);
            default:
                return vectorizeWordFrequency(text);
        }
    }
    
    /**
     * 基于字符频率的向量化
     */
    private double[] vectorizeCharacterFrequency(String text) {
        double[] vector = new double[128]; // ASCII字符集
        for (char c : text.toLowerCase().toCharArray()) {
            if (c < 128) {
                vector[c]++;
            }
        }
        return normalize(vector);
    }
    
    /**
     * 基于词频的向量化
     */
    private double[] vectorizeWordFrequency(String text) {
        double[] vector = new double[vocabulary.size()];
        Set<String> words = extractWords(text);
        
        for (String word : words) {
            Integer index = vocabulary.get(word);
            if (index != null) {
                vector[index]++;
            }
        }
        
        return normalize(vector);
    }
    
    /**
     * 基于TF-IDF的向量化
     */
    private double[] vectorizeTFIDF(String text) {
        double[] vector = new double[vocabulary.size()];
        Set<String> words = extractWords(text);
        
        for (String word : words) {
            Integer index = vocabulary.get(word);
            if (index != null) {
                vector[index]++;
            }
        }
        
        // 简单的TF-IDF计算
        for (int i = 0; i < vector.length; i++) {
            if (vector[i] > 0) {
                vector[i] = 1 + Math.log(vector[i]);
            }
        }
        
        return normalize(vector);
    }
    
    /**
     * 基于简单哈希的向量化
     */
    private double[] vectorizeSimpleHash(String text) {
        double[] vector = new double[256]; // 使用256维向量
        for (int i = 0; i < text.length(); i++) {
            int hash = Math.abs(text.charAt(i) * 31 + i) % 256;
            vector[hash]++;
        }
        return normalize(vector);
    }
    
    /**
     * 提取文本中的词汇
     */
    private Set<String> extractWords(String text) {
        return Arrays.stream(text.toLowerCase()
                .replaceAll("[^a-zA-Z\\s]", " ")
                .split("\\s+"))
                .filter(word -> word.length() > 1)
                .collect(Collectors.toSet());
    }
    
    /**
     * 向量归一化
     */
    private double[] normalize(double[] vector) {
        double magnitude = 0.0;
        for (double v : vector) {
            magnitude += v * v;
        }
        magnitude = Math.sqrt(magnitude);
        
        if (magnitude > 0) {
            for (int i = 0; i < vector.length; i++) {
                vector[i] /= magnitude;
            }
        }
        
        return vector;
    }
    
    public Map<String, Integer> getVocabulary() {
        return Collections.unmodifiableMap(vocabulary);
    }
    
    public int getVectorDimension() {
        return vocabulary.size();
    }
    
    /**
     * 向量化策略枚举
     */
    public enum VectorizationStrategy {
        TF_IDF,
        CHARACTER_FREQUENCY,
        WORD_FREQUENCY,
        SIMPLE_HASH
    }
} 
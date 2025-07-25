package com.github.kuangcp.vector;

import java.util.Arrays;

/**
 * 向量相似度计算工具类
 */
public class SimilarityCalculator {
    
    /**
     * 计算余弦相似度
     * @param vector1 向量1
     * @param vector2 向量2
     * @return 余弦相似度值 (0-1之间，1表示完全相同)
     */
    public static double cosineSimilarity(double[] vector1, double[] vector2) {
        if (vector1 == null || vector2 == null) {
            return 0.0;
        }
        
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("向量维度必须相同");
        }
        
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;
        
        for (int i = 0; i < vector1.length; i++) {
            dotProduct += vector1[i] * vector2[i];
            norm1 += vector1[i] * vector1[i];
            norm2 += vector2[i] * vector2[i];
        }
        
        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }
        
        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
    
    /**
     * 计算欧几里得距离
     * @param vector1 向量1
     * @param vector2 向量2
     * @return 欧几里得距离
     */
    public static double euclideanDistance(double[] vector1, double[] vector2) {
        if (vector1 == null || vector2 == null) {
            return Double.MAX_VALUE;
        }
        
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("向量维度必须相同");
        }
        
        double sum = 0.0;
        for (int i = 0; i < vector1.length; i++) {
            double diff = vector1[i] - vector2[i];
            sum += diff * diff;
        }
        
        return Math.sqrt(sum);
    }
    
    /**
     * 计算曼哈顿距离
     * @param vector1 向量1
     * @param vector2 向量2
     * @return 曼哈顿距离
     */
    public static double manhattanDistance(double[] vector1, double[] vector2) {
        if (vector1 == null || vector2 == null) {
            return Double.MAX_VALUE;
        }
        
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("向量维度必须相同");
        }
        
        double sum = 0.0;
        for (int i = 0; i < vector1.length; i++) {
            sum += Math.abs(vector1[i] - vector2[i]);
        }
        
        return sum;
    }
    
    /**
     * 计算杰卡德相似度（用于集合）
     * @param set1 集合1
     * @param set2 集合2
     * @return 杰卡德相似度
     */
    public static double jaccardSimilarity(java.util.Set<String> set1, java.util.Set<String> set2) {
        if (set1 == null || set2 == null) {
            return 0.0;
        }
        
        java.util.Set<String> intersection = new java.util.HashSet<>(set1);
        intersection.retainAll(set2);
        
        java.util.Set<String> union = new java.util.HashSet<>(set1);
        union.addAll(set2);
        
        if (union.isEmpty()) {
            return 0.0;
        }
        
        return (double) intersection.size() / union.size();
    }
    
    /**
     * 将距离转换为相似度分数
     * @param distance 距离值
     * @param maxDistance 最大距离
     * @return 相似度分数 (0-1之间)
     */
    public static double distanceToSimilarity(double distance, double maxDistance) {
        if (maxDistance <= 0) {
            return 0.0;
        }
        return Math.max(0.0, 1.0 - (distance / maxDistance));
    }
    
    /**
     * 计算向量的模长
     * @param vector 向量
     * @return 模长
     */
    public static double vectorMagnitude(double[] vector) {
        if (vector == null) {
            return 0.0;
        }
        
        double sum = 0.0;
        for (double v : vector) {
            sum += v * v;
        }
        return Math.sqrt(sum);
    }
    
    /**
     * 向量归一化
     * @param vector 输入向量
     * @return 归一化后的向量
     */
    public static double[] normalizeVector(double[] vector) {
        if (vector == null) {
            return null;
        }
        
        double magnitude = vectorMagnitude(vector);
        if (magnitude == 0.0) {
            return Arrays.copyOf(vector, vector.length);
        }
        
        double[] normalized = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            normalized[i] = vector[i] / magnitude;
        }
        
        return normalized;
    }
} 
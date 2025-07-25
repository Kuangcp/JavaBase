package com.github.kuangcp.vector;

/**
 * 向量匹配结果
 */
public class MatchResult implements Comparable<MatchResult> {
    
    private final String term;
    private final double similarity;
    private final double[] vector;
    
    public MatchResult(String term, double similarity, double[] vector) {
        this.term = term;
        this.similarity = similarity;
        this.vector = vector != null ? vector.clone() : null;
    }
    
    public MatchResult(String term, double similarity) {
        this(term, similarity, null);
    }
    
    public String getTerm() {
        return term;
    }
    
    public double getSimilarity() {
        return similarity;
    }
    
    public double getConfidence() {
        return similarity; // 置信度就是相似度
    }
    
    public double[] getVector() {
        return vector != null ? vector.clone() : null;
    }
    
    @Override
    public int compareTo(MatchResult other) {
        // 按相似度降序排列
        return Double.compare(other.similarity, this.similarity);
    }
    
    @Override
    public String toString() {
        return String.format("MatchResult{term='%s', similarity=%.4f, confidence=%.2f%%}", 
                term, similarity, similarity * 100);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        MatchResult that = (MatchResult) obj;
        return Double.compare(that.similarity, similarity) == 0 && 
               term.equals(that.term);
    }
    
    @Override
    public int hashCode() {
        int result = term.hashCode();
        result = 31 * result + Double.hashCode(similarity);
        return result;
    }
} 
package com.github.kuangcp.vector;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * 向量匹配系统测试
 */
public class VectorTest {
    
    private VectorMatcher basicMatcher;
    private AdvancedVectorMatcher advancedMatcher;
    private List<String> technicalTerms;
    
    @Before
    public void setUp() {
        // 初始化专业术语列表
        technicalTerms = Arrays.asList(
            "机器学习", "深度学习", "神经网络", "卷积神经网络", "循环神经网络",
            "自然语言处理", "计算机视觉", "强化学习", "监督学习", "无监督学习",
            "半监督学习", "迁移学习", "集成学习", "随机森林", "支持向量机",
            "决策树", "逻辑回归", "线性回归", "聚类算法", "降维算法",
            "特征工程", "数据预处理", "模型评估", "交叉验证", "过拟合",
            "欠拟合", "正则化", "梯度下降", "反向传播", "激活函数",
            "损失函数", "优化器", "批量归一化", "Dropout", "注意力机制",
            "Transformer", "BERT", "GPT", "ResNet", "VGG", "AlexNet",
            "YOLO", "R-CNN", "Fast R-CNN", "Faster R-CNN", "Mask R-CNN",
            "目标检测", "图像分割", "语义分割", "实例分割", "姿态估计"
        );
        
        // 初始化基础匹配器
        basicMatcher = new VectorMatcher(Vectorizer.VectorizationStrategy.TF_IDF);
        basicMatcher.initialize(technicalTerms);
        
        // 初始化高级匹配器
        advancedMatcher = new AdvancedVectorMatcher(AdvancedVectorMatcher.MatchingStrategy.WEIGHTED_ENSEMBLE);
        advancedMatcher.initialize(technicalTerms);
    }
    
    @Test
    public void testBasicVectorMatching() {
        String input = "机器学习算法";
        List<MatchResult> results = basicMatcher.findSimilarTerms(input, 5, 0.1);
        
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertTrue(results.size() <= 5);
        
        // 验证结果按相似度降序排列
        for (int i = 1; i < results.size(); i++) {
            assertTrue(results.get(i-1).getSimilarity() >= results.get(i).getSimilarity());
        }
        
        // 验证置信度
        for (MatchResult result : results) {
            assertTrue(result.getConfidence() >= 0.1);
            assertTrue(result.getConfidence() <= 1.0);
        }
        
        System.out.println("基础匹配结果:");
        results.forEach(System.out::println);
    }
    
    @Test
    public void testAdvancedVectorMatching() {
        String input = "深度学习网络";
        List<MatchResult> results = advancedMatcher.findSimilarTerms(input, 5, 0.1);
        
        assertNotNull(results);
        assertFalse(results.isEmpty());
        
        System.out.println("高级匹配结果:");
        results.forEach(System.out::println);
    }
    
    @Test
    public void testDifferentVectorizationStrategies() {
        String input = "神经网络";
        
        // 测试不同的向量化策略
        Vectorizer.VectorizationStrategy[] strategies = {
            Vectorizer.VectorizationStrategy.TF_IDF,
            Vectorizer.VectorizationStrategy.CHARACTER_FREQUENCY,
            Vectorizer.VectorizationStrategy.WORD_FREQUENCY,
            Vectorizer.VectorizationStrategy.SIMPLE_HASH
        };
        
        for (Vectorizer.VectorizationStrategy strategy : strategies) {
            VectorMatcher matcher = new VectorMatcher(strategy);
            matcher.initialize(technicalTerms);
            
            List<MatchResult> results = matcher.findSimilarTerms(input, 3, 0.1);
            
            System.out.println("\n策略: " + strategy);
            results.forEach(System.out::println);
            
            assertNotNull(results);
        }
    }
    
    @Test
    public void testBatchMatching() {
        List<String> inputs = Arrays.asList(
            "机器学习", "深度学习", "神经网络", "计算机视觉", "自然语言处理"
        );
        
        Map<String, List<MatchResult>> batchResults = advancedMatcher.batchFindSimilarTerms(inputs, 3, 0.1);
        
        assertNotNull(batchResults);
        assertEquals(inputs.size(), batchResults.size());
        
        System.out.println("批量匹配结果:");
        batchResults.forEach((input, results) -> {
            System.out.println("\n输入: " + input);
            results.forEach(System.out::println);
        });
    }
    
    @Test
    public void testSimilarityCalculation() {
        double[] vector1 = {1.0, 0.0, 0.0};
        double[] vector2 = {0.0, 1.0, 0.0};
        double[] vector3 = {1.0, 0.0, 0.0};
        
        // 测试余弦相似度
        double similarity1 = SimilarityCalculator.cosineSimilarity(vector1, vector2);
        double similarity2 = SimilarityCalculator.cosineSimilarity(vector1, vector3);
        
        assertEquals(0.0, similarity1, 0.001); // 垂直向量相似度为0
        assertEquals(1.0, similarity2, 0.001); // 相同向量相似度为1
        
        // 测试欧几里得距离
        double distance1 = SimilarityCalculator.euclideanDistance(vector1, vector2);
        double distance2 = SimilarityCalculator.euclideanDistance(vector1, vector3);
        
        assertEquals(Math.sqrt(2), distance1, 0.001);
        assertEquals(0.0, distance2, 0.001);
    }
    
    @Test
    public void testVectorization() {
        Vectorizer vectorizer = new Vectorizer(Vectorizer.VectorizationStrategy.TF_IDF);
        vectorizer.buildVocabulary(Arrays.asList("机器学习", "深度学习", "神经网络"));
        
        double[] vector = vectorizer.vectorize("机器学习");
        
        assertNotNull(vector);
        assertTrue(vector.length > 0);
        
        // 验证向量归一化
        double magnitude = SimilarityCalculator.vectorMagnitude(vector);
        assertTrue(Math.abs(magnitude - 1.0) < 0.001 || magnitude == 0.0);
    }
    
    @Test
    public void testEdgeCases() {
        // 测试空输入
        List<MatchResult> emptyResults = basicMatcher.findSimilarTerms("", 5, 0.1);
        assertTrue(emptyResults.isEmpty());
        
        // 测试null输入
        List<MatchResult> nullResults = basicMatcher.findSimilarTerms(null, 5, 0.1);
        assertTrue(nullResults.isEmpty());
        
        // 测试不存在的术语
        List<MatchResult> noMatchResults = basicMatcher.findSimilarTerms("不存在的术语", 5, 0.9);
        assertTrue(noMatchResults.isEmpty());
    }
    
    @Test
    public void testPerformance() {
        String input = "机器学习算法";
        int iterations = 1000;
        
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            basicMatcher.findSimilarTerms(input, 5, 0.1);
        }
        long endTime = System.currentTimeMillis();
        
        long duration = endTime - startTime;
        double avgTime = (double) duration / iterations;
        
        System.out.println("性能测试结果:");
        System.out.println("总耗时: " + duration + "ms");
        System.out.println("平均耗时: " + avgTime + "ms");
        System.out.println("QPS: " + (1000.0 / avgTime));
        
        // 验证性能在合理范围内
        assertTrue(avgTime < 10.0); // 平均每次查询应该小于10ms
    }
    
    @Test
    public void testRealWorldScenario() {
        // 模拟实际应用场景
        String[] queries = {
            "深度学习模型",
            "神经网络训练",
            "机器学习算法",
            "计算机视觉技术",
            "自然语言处理系统",
            "强化学习环境",
            "卷积神经网络架构",
            "注意力机制实现",
            "目标检测算法",
            "图像分割方法"
        };
        
        System.out.println("实际应用场景测试结果:");
        for (String query : queries) {
            List<MatchResult> results = advancedMatcher.findSimilarTerms(query, 3, 0.2);
            System.out.println("\n查询: " + query);
            if (results.isEmpty()) {
                System.out.println("  无匹配结果");
            } else {
                results.forEach(result -> 
                    System.out.println("  " + result.getTerm() + " (置信度: " + 
                                     String.format("%.2f%%", result.getConfidence() * 100) + ")")
                );
            }
        }
    }
}

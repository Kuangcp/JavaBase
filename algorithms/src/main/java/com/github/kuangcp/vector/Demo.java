package com.github.kuangcp.vector;

import java.util.Arrays;
import java.util.List;

/**
 * 向量匹配系统演示程序
 */
public class Demo {
    
    public static void main(String[] args) {
        System.out.println("=== Java向量匹配系统演示 ===\n");
        
        // 1. 准备专业术语数据
        List<String> technicalTerms = Arrays.asList(
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
        
        System.out.println("术语库大小: " + technicalTerms.size() + " 个专业术语");
        
        // 2. 创建向量匹配器
        System.out.println("\n正在初始化向量匹配器...");
        VectorMatcher matcher = new VectorMatcher(Vectorizer.VectorizationStrategy.TF_IDF);
        matcher.initialize(technicalTerms);
        System.out.println("初始化完成！向量维度: " + matcher.getVectorDimension());
        
        // 3. 演示相似度匹配
        System.out.println("\n=== 相似度匹配演示 ===");
        
        String[] testQueries = {
            "机器学习算法",
            "深度学习模型", 
            "神经网络训练",
            "计算机视觉技术",
            "自然语言处理系统"
        };
        
        for (String query : testQueries) {
            System.out.println("\n查询: " + query);
            List<MatchResult> results = matcher.findSimilarTerms(query, 5, 0.1);
            
            if (results.isEmpty()) {
                System.out.println("  无匹配结果");
            } else {
                System.out.println("  匹配结果:");
                for (int i = 0; i < results.size(); i++) {
                    MatchResult result = results.get(i);
                    System.out.printf("    %d. %s (相似度: %.4f, 置信度: %.2f%%)\n", 
                        i + 1, result.getTerm(), result.getSimilarity(), 
                        result.getConfidence() * 100);
                }
            }
        }
        
        // 4. 演示高级匹配器
        System.out.println("\n=== 高级匹配器演示 ===");
        AdvancedVectorMatcher advancedMatcher = new AdvancedVectorMatcher(
            AdvancedVectorMatcher.MatchingStrategy.WEIGHTED_ENSEMBLE
        );
        advancedMatcher.initialize(technicalTerms);
        
        String advancedQuery = "深度学习网络";
        List<MatchResult> advancedResults = advancedMatcher.findSimilarTerms(advancedQuery, 3, 0.2);
        
        System.out.println("查询: " + advancedQuery);
        System.out.println("高级匹配结果:");
        for (int i = 0; i < advancedResults.size(); i++) {
            MatchResult result = advancedResults.get(i);
            System.out.printf("  %d. %s (置信度: %.2f%%)\n", 
                i + 1, result.getTerm(), result.getConfidence() * 100);
        }
        
        // 5. 演示不同策略的效果
        System.out.println("\n=== 不同向量化策略对比 ===");
        String compareQuery = "神经网络";
        
        Vectorizer.VectorizationStrategy[] strategies = {
            Vectorizer.VectorizationStrategy.TF_IDF,
            Vectorizer.VectorizationStrategy.CHARACTER_FREQUENCY,
            Vectorizer.VectorizationStrategy.WORD_FREQUENCY,
            Vectorizer.VectorizationStrategy.SIMPLE_HASH
        };
        
        for (Vectorizer.VectorizationStrategy strategy : strategies) {
            VectorMatcher strategyMatcher = new VectorMatcher(strategy);
            strategyMatcher.initialize(technicalTerms);
            
            List<MatchResult> strategyResults = strategyMatcher.findSimilarTerms(compareQuery, 3, 0.1);
            
            System.out.println("\n策略: " + strategy);
            for (int i = 0; i < strategyResults.size(); i++) {
                MatchResult result = strategyResults.get(i);
                System.out.printf("  %d. %s (相似度: %.4f)\n", 
                    i + 1, result.getTerm(), result.getSimilarity());
            }
        }
        
        // 6. 性能演示
        System.out.println("\n=== 性能演示 ===");
        String performanceQuery = "机器学习算法";
        int iterations = 1000;
        
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            matcher.findSimilarTerms(performanceQuery, 5, 0.1);
        }
        long endTime = System.currentTimeMillis();
        
        long duration = endTime - startTime;
        double avgTime = (double) duration / iterations;
        double qps = 1000.0 / avgTime;
        
        System.out.println("性能测试结果:");
        System.out.println("  术语数量: " + technicalTerms.size());
        System.out.println("  测试查询: " + performanceQuery);
        System.out.println("  迭代次数: " + iterations);
        System.out.println("  总耗时: " + duration + "ms");
        System.out.println("  平均耗时: " + String.format("%.2f", avgTime) + "ms");
        System.out.println("  QPS: " + String.format("%.2f", qps));
        
        System.out.println("\n=== 演示完成 ===");
        System.out.println("这个向量匹配系统可以用于:");
        System.out.println("1. 智能客服系统的术语匹配");
        System.out.println("2. 搜索引擎的相关词推荐");
        System.out.println("3. 知识图谱的实体链接");
        System.out.println("4. 文本分类和聚类");
        System.out.println("5. 推荐系统的内容匹配");
    }
} 
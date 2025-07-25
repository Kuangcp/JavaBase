package com.github.kuangcp.vector;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 向量匹配系统使用示例
 * 展示如何在实际项目中使用向量匹配功能
 */
public class VectorMatchingExample {
    
    public static void main(String[] args) {
        // 示例1：基础使用
        basicUsageExample();
        
        System.out.println("\n" + String.join("", Collections.nCopies(50, "=")) + "\n");
        
        // 示例2：高级使用
        advancedUsageExample();
        
        System.out.println("\n" + String.join("", Collections.nCopies(50, "=")) + "\n");
        
        // 示例3：不同策略对比
        strategyComparisonExample();
        
        System.out.println("\n" + String.join("", Collections.nCopies(50, "=")) + "\n");
        
        // 示例4：实际应用场景
        realWorldExample();
    }
    
    /**
     * 基础使用示例
     */
    public static void basicUsageExample() {
        System.out.println("=== 基础使用示例 ===");
        
        // 1. 准备专业术语列表
        List<String> terms = Arrays.asList(
            "机器学习", "深度学习", "神经网络", "卷积神经网络", "循环神经网络",
            "自然语言处理", "计算机视觉", "强化学习", "监督学习", "无监督学习",
            "支持向量机", "决策树", "逻辑回归", "线性回归", "聚类算法"
        );
        
        // 2. 创建向量匹配器
        VectorMatcher matcher = new VectorMatcher(Vectorizer.VectorizationStrategy.TF_IDF);
        
        // 3. 初始化匹配器
        matcher.initialize(terms);
        
        // 4. 进行相似度匹配
        String query = "机器学习算法";
        List<MatchResult> results = matcher.findSimilarTerms(query, 5, 0.1);
        
        // 5. 输出结果
        System.out.println("查询: " + query);
        System.out.println("匹配结果:");
        for (int i = 0; i < results.size(); i++) {
            MatchResult result = results.get(i);
            System.out.printf("  %d. %s (相似度: %.4f, 置信度: %.2f%%)\n", 
                i + 1, result.getTerm(), result.getSimilarity(), 
                result.getConfidence() * 100);
        }
    }
    
    /**
     * 高级使用示例
     */
    public static void advancedUsageExample() {
        System.out.println("=== 高级使用示例 ===");
        
        // 1. 准备更丰富的术语列表
        List<String> terms = Arrays.asList(
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
        
        // 2. 创建高级匹配器
        AdvancedVectorMatcher matcher = new AdvancedVectorMatcher(
            AdvancedVectorMatcher.MatchingStrategy.WEIGHTED_ENSEMBLE
        );
        
        // 3. 初始化匹配器
        matcher.initialize(terms);
        
        // 4. 进行批量查询
        List<String> queries = Arrays.asList(
            "深度学习模型", "神经网络训练", "计算机视觉技术"
        );
        
        System.out.println("批量查询结果:");
        for (String query : queries) {
            List<MatchResult> results = matcher.findSimilarTerms(query, 3, 0.2);
            System.out.println("\n查询: " + query);
            if (results.isEmpty()) {
                System.out.println("  无匹配结果");
            } else {
                for (int i = 0; i < results.size(); i++) {
                    MatchResult result = results.get(i);
                    System.out.printf("  %d. %s (置信度: %.2f%%)\n", 
                        i + 1, result.getTerm(), result.getConfidence() * 100);
                }
            }
        }
    }
    
    /**
     * 不同策略对比示例
     */
    public static void strategyComparisonExample() {
        System.out.println("=== 不同策略对比示例 ===");
        
        List<String> terms = Arrays.asList(
            "机器学习", "深度学习", "神经网络", "卷积神经网络", "循环神经网络",
            "自然语言处理", "计算机视觉", "强化学习", "监督学习", "无监督学习"
        );
        
        String query = "神经网络";
        
        // 测试不同的向量化策略
        Vectorizer.VectorizationStrategy[] strategies = {
            Vectorizer.VectorizationStrategy.TF_IDF,
            Vectorizer.VectorizationStrategy.CHARACTER_FREQUENCY,
            Vectorizer.VectorizationStrategy.WORD_FREQUENCY,
            Vectorizer.VectorizationStrategy.SIMPLE_HASH
        };
        
        for (Vectorizer.VectorizationStrategy strategy : strategies) {
            VectorMatcher matcher = new VectorMatcher(strategy);
            matcher.initialize(terms);
            
            List<MatchResult> results = matcher.findSimilarTerms(query, 3, 0.1);
            
            System.out.println("\n策略: " + strategy);
            if (results.isEmpty()) {
                System.out.println("  无匹配结果");
            } else {
                for (int i = 0; i < results.size(); i++) {
                    MatchResult result = results.get(i);
                    System.out.printf("  %d. %s (相似度: %.4f)\n", 
                        i + 1, result.getTerm(), result.getSimilarity());
                }
            }
        }
    }
    
    /**
     * 实际应用场景示例
     */
    public static void realWorldExample() {
        System.out.println("=== 实际应用场景示例 ===");
        
        // 模拟一个智能客服系统的术语匹配
        List<String> technicalTerms = Arrays.asList(
            // 机器学习相关
            "机器学习", "深度学习", "神经网络", "监督学习", "无监督学习",
            "强化学习", "迁移学习", "集成学习", "随机森林", "支持向量机",
            "决策树", "逻辑回归", "线性回归", "聚类算法", "降维算法",
            
            // 深度学习框架
            "TensorFlow", "PyTorch", "Keras", "Scikit-learn", "XGBoost",
            "LightGBM", "CatBoost", "Theano", "Caffe", "MXNet",
            
            // 计算机视觉
            "计算机视觉", "图像识别", "目标检测", "图像分割", "人脸识别",
            "OCR", "图像处理", "特征提取", "边缘检测", "图像增强",
            
            // 自然语言处理
            "自然语言处理", "文本分类", "情感分析", "命名实体识别", "机器翻译",
            "问答系统", "文本摘要", "语言模型", "词向量", "句向量",
            
            // 数据处理
            "数据预处理", "特征工程", "数据清洗", "数据标准化", "数据归一化",
            "缺失值处理", "异常值检测", "数据增强", "特征选择", "特征提取"
        );
        
        // 创建匹配器
        AdvancedVectorMatcher matcher = new AdvancedVectorMatcher(
            AdvancedVectorMatcher.MatchingStrategy.ENSEMBLE
        );
        matcher.initialize(technicalTerms);
        
        // 模拟用户查询
        String[] userQueries = {
            "我想了解机器学习",
            "深度学习框架有哪些",
            "如何处理图像数据",
            "文本分析怎么做",
            "数据预处理步骤"
        };
        
        System.out.println("智能客服术语匹配示例:");
        for (String query : userQueries) {
            List<MatchResult> results = matcher.findSimilarTerms(query, 5, 0.15);
            
            System.out.println("\n用户查询: " + query);
            System.out.println("推荐术语:");
            
            if (results.isEmpty()) {
                System.out.println("  暂无相关术语");
            } else {
                for (int i = 0; i < results.size(); i++) {
                    MatchResult result = results.get(i);
                    System.out.printf("  %d. %s (相关度: %.2f%%)\n", 
                        i + 1, result.getTerm(), result.getConfidence() * 100);
                }
            }
        }
    }
    
    /**
     * 性能测试示例
     */
    public static void performanceTest() {
        System.out.println("=== 性能测试示例 ===");
        
        // 创建大量术语进行性能测试
        List<String> largeTermSet = Arrays.asList(
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
        
        VectorMatcher matcher = new VectorMatcher(Vectorizer.VectorizationStrategy.TF_IDF);
        matcher.initialize(largeTermSet);
        
        String testQuery = "机器学习算法";
        int iterations = 1000;
        
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            matcher.findSimilarTerms(testQuery, 10, 0.1);
        }
        long endTime = System.currentTimeMillis();
        
        long duration = endTime - startTime;
        double avgTime = (double) duration / iterations;
        double qps = 1000.0 / avgTime;
        
        System.out.println("性能测试结果:");
        System.out.println("术语数量: " + largeTermSet.size());
        System.out.println("测试查询: " + testQuery);
        System.out.println("迭代次数: " + iterations);
        System.out.println("总耗时: " + duration + "ms");
        System.out.println("平均耗时: " + String.format("%.2f", avgTime) + "ms");
        System.out.println("QPS: " + String.format("%.2f", qps));
    }
} 
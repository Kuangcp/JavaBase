# Java向量匹配系统

这是一个基于Java实现的多维度向量数据匹配系统，专门用于专业术语的向量化和相似度匹配。

## 功能特性

- **多种向量化策略**: 支持TF-IDF、字符频率、词频、简单哈希等多种向量化方法
- **余弦相似度计算**: 使用余弦相似度进行向量匹配
- **高级匹配策略**: 支持集成学习、最佳策略、加权集成等高级匹配方法
- **高性能**: 支持批量查询和并行处理
- **易于使用**: 提供简洁的API接口
- **可扩展**: 支持自定义向量化策略和相似度计算方法

## 核心组件

### 1. Vectorizer (向量化工具类)
负责将文本转换为向量表示，支持多种向量化策略：

- `TF_IDF`: 基于词频-逆文档频率的向量化
- `CHARACTER_FREQUENCY`: 基于字符频率的向量化
- `WORD_FREQUENCY`: 基于词频的向量化
- `SIMPLE_HASH`: 基于简单哈希的向量化

### 2. SimilarityCalculator (相似度计算工具类)
提供多种相似度计算方法：

- 余弦相似度 (cosine similarity)
- 欧几里得距离 (euclidean distance)
- 曼哈顿距离 (manhattan distance)
- 杰卡德相似度 (jaccard similarity)

### 3. VectorMatcher (基础向量匹配器)
提供基础的向量匹配功能：

- 术语向量化
- 相似度计算
- 结果排序和过滤

### 4. AdvancedVectorMatcher (高级向量匹配器)
提供高级匹配功能：

- 多种匹配策略集成
- 并行处理
- 缓存优化

### 5. MatchResult (匹配结果类)
封装匹配结果信息：

- 匹配的术语
- 相似度分数
- 置信度

## 快速开始

### 1. 基础使用

```java
// 准备专业术语列表
List<String> terms = Arrays.asList(
    "机器学习", "深度学习", "神经网络", "卷积神经网络", "循环神经网络",
    "自然语言处理", "计算机视觉", "强化学习", "监督学习", "无监督学习"
);

// 创建向量匹配器
VectorMatcher matcher = new VectorMatcher(Vectorizer.VectorizationStrategy.TF_IDF);

// 初始化匹配器
matcher.initialize(terms);

// 进行相似度匹配
String query = "机器学习算法";
List<MatchResult> results = matcher.findSimilarTerms(query, 5, 0.1);

// 输出结果
for (MatchResult result : results) {
    System.out.println(result.getTerm() + " (置信度: " + 
                      String.format("%.2f%%", result.getConfidence() * 100) + ")");
}
```

### 2. 高级使用

```java
// 创建高级匹配器
AdvancedVectorMatcher advancedMatcher = new AdvancedVectorMatcher(
    AdvancedVectorMatcher.MatchingStrategy.WEIGHTED_ENSEMBLE
);

// 初始化
advancedMatcher.initialize(terms);

// 批量查询
List<String> queries = Arrays.asList("深度学习模型", "神经网络训练");
Map<String, List<MatchResult>> batchResults = 
    advancedMatcher.batchFindSimilarTerms(queries, 3, 0.2);

// 输出批量结果
batchResults.forEach((query, results) -> {
    System.out.println("查询: " + query);
    results.forEach(result -> 
        System.out.println("  " + result.getTerm() + " (置信度: " + 
                          String.format("%.2f%%", result.getConfidence() * 100) + ")")
    );
});
```

### 3. 不同策略对比

```java
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
    System.out.println("策略: " + strategy);
    results.forEach(result -> 
        System.out.println("  " + result.getTerm() + " (相似度: " + 
                          String.format("%.4f", result.getSimilarity()) + ")")
    );
}
```

## 应用场景

### 1. 智能客服系统
- 用户输入问题，系统匹配相关专业术语
- 提供准确的答案和建议

### 2. 搜索引擎
- 相关词推荐
- 查询扩展
- 搜索结果排序

### 3. 知识图谱
- 实体链接
- 关系抽取
- 知识推理

### 4. 文本分类和聚类
- 文档相似度计算
- 主题建模
- 内容推荐

### 5. 推荐系统
- 内容匹配
- 用户兴趣建模
- 协同过滤

## 性能优化

### 1. 向量维度控制
- 词汇表大小限制
- 特征选择
- 降维处理

### 2. 缓存机制
- 向量缓存
- 结果缓存
- 词汇表缓存

### 3. 并行处理
- 批量查询并行化
- 向量计算并行化
- 多线程优化

### 4. 索引优化
- 向量索引
- 相似度索引
- 快速检索

## 配置参数

### 向量化参数
- `vocabularySize`: 词汇表大小 (默认: 1000)
- `minWordLength`: 最小词长度 (默认: 2)
- `vectorDimension`: 向量维度

### 匹配参数
- `topK`: 返回结果数量
- `minSimilarity`: 最小相似度阈值
- `matchingStrategy`: 匹配策略

### 性能参数
- `cacheSize`: 缓存大小
- `threadPoolSize`: 线程池大小
- `batchSize`: 批处理大小

## 扩展开发

### 1. 自定义向量化策略
```java
public class CustomVectorizer extends Vectorizer {
    @Override
    public double[] vectorize(String text) {
        // 实现自定义向量化逻辑
        return customVector;
    }
}
```

### 2. 自定义相似度计算
```java
public class CustomSimilarityCalculator {
    public static double customSimilarity(double[] vector1, double[] vector2) {
        // 实现自定义相似度计算
        return similarity;
    }
}
```

### 3. 自定义匹配策略
```java
public class CustomMatcher extends VectorMatcher {
    @Override
    public List<MatchResult> findSimilarTerms(String input, int topK, double minSimilarity) {
        // 实现自定义匹配逻辑
        return customResults;
    }
}
```

## 测试和验证

### 运行测试
```bash
mvn test -Dtest=VectorTest
```

### 运行演示
```bash
mvn exec:java -Dexec.mainClass="com.github.kuangcp.vector.Demo"
```

### 性能测试
```bash
mvn test -Dtest=VectorTest#testPerformance
```

## 注意事项

1. **内存使用**: 大量术语时注意内存占用
2. **向量维度**: 高维向量可能影响性能
3. **相似度阈值**: 合理设置阈值避免过多无关结果
4. **词汇表更新**: 动态添加术语时需要重新构建词汇表
5. **线程安全**: 多线程环境下注意并发安全

## 依赖要求

- Java 8+
- Maven 3.6+
- JUnit 4 (测试)

## 许可证

MIT License

## 贡献

欢迎提交Issue和Pull Request来改进这个项目。 
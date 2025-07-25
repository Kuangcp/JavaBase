package com.github.kuangcp.string;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Kuangcp
 * 2025-07-07 10:24
 */
public class ACMatcherTest {

    @Test
    public void testMatch() throws Exception {
        // 初始化字典
        Set<String> dictionary = new HashSet<>(Arrays.asList("鸡胸", "性病", "感冒"));
        ACMatcher matcher = new ACMatcher();
        matcher.buildTrie(dictionary);

// 测试文本
        String text = "这个鸡胸肉很好吃，但是感冒药吃多了对身体不好，器质性病变需要及时治疗";
        List<ACMatcher.Match> matches = matcher.findMatches(text);

// 输出匹配结果
        for (ACMatcher.Match match : matches) {
            System.out.printf("找到匹配：%s (位置：%d-%d)%n",
                    match.word, match.start, match.end);
        }
    }
}

package com.github.kuangcp.string;

import org.junit.Test;

import java.util.function.BiFunction;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author kuangchengping@sinohealth.cn
 * 2023-10-26 13:49
 */
public class NumberMaxDiffIndexTest {

    @Test
    public void testDiffIndex() throws Exception {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            runAndAssert(NumberMaxDiffIndex::findDiffIndex);
        }
        long end = System.currentTimeMillis();
        System.out.println("split: " + (end - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            runAndAssert(NumberMaxDiffIndex::findDiffIndexPoint);
        }
        end = System.currentTimeMillis();
        System.out.println("index: "+(end - start));
    }

    private static void runAndAssert(BiFunction<String, String, Integer> func) {
        assertThat(func.apply("123.0", "123"), equalTo(0));
        assertThat(func.apply("123.0", "121"), equalTo(1));
        assertThat(func.apply("123.0", "113"), equalTo(2));
        assertThat(func.apply("123.0", "323"), equalTo(3));
        assertThat(func.apply("123.0", "1123"), equalTo(4));

        assertThat(func.apply("123.34", "123"), equalTo(-1));
        assertThat(func.apply("123.3", "123"), equalTo(-1));
        assertThat(func.apply("123.3", "120.31"), equalTo(1));
        assertThat(func.apply("123.3", "110.31"), equalTo(2));
        assertThat(func.apply("123.3", "12.31"), equalTo(3));
        assertThat(func.apply("12.3", "12.31"), equalTo(-2));
        assertThat(func.apply("12.3", "12.30"), equalTo(0));
        assertThat(func.apply("12.3", ""), equalTo(2));
        assertThat(func.apply("12.3", null), equalTo(2));
        assertThat(func.apply(null, "12.3"), equalTo(2));
    }
}

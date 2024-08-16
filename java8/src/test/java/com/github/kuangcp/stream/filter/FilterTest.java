package com.github.kuangcp.stream.filter;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author kuangcp on 3/6/19-5:08 PM
 */
@Slf4j
public class FilterTest {

    // 当改成 private 时, 才会在下面的方法警告在拆箱时可能导致NPE, Idea 2018 3.5
    public Boolean isTrue(int count) {
        if (count < 5) {
            return false;
        }
        if (count == 6) {
            return null;
        }
        return true;
    }

    @Test(expected = NullPointerException.class)
    public void testFilterWithNull() {
        long count = IntStream.rangeClosed(1, 10).filter(this::isTrue).count();
        System.out.println(count);
    }

    // 多个 filter 具有 && 一样的短路效应
    @Test
    public void testAnd() {
        List<String> data = Arrays.asList("", null, "2", "12");
        long count = data.stream()
                .filter(Objects::nonNull)
                .filter(v -> v.length() > 1)
                .count();
        assertThat(count, equalTo(1L));
    }
}

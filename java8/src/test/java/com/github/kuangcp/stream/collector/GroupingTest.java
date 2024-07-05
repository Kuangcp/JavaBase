package com.github.kuangcp.stream.collector;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Kuangcp
 * 2024-06-28 14:26
 */
@Slf4j
public class GroupingTest {

    @Test
    public void testSecondHandle() throws Exception {
        Map<Integer, List<Boolean>> groupMapedMap = Stream.of("aa", "abb", "CCC")
                .collect(Collectors.groupingBy(String::length,
                        Collectors.mapping(v -> {
                            return v.startsWith("a");
                        }, Collectors.toList())));
        log.info("result={}", groupMapedMap);
    }

    @Test
    public void testSimple() throws Exception {
        Map<Integer, List<String>> lenMap = Stream.of("aa", "bb", "CCC").collect(Collectors.groupingBy(String::length));
        log.info("result={}", lenMap);
    }
}

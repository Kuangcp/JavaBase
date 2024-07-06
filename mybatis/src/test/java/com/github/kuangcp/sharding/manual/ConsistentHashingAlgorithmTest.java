package com.github.kuangcp.sharding.manual;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2021-07-11 19:32
 */
@Slf4j
public class ConsistentHashingAlgorithmTest {

    private Map<Integer, String> userMap = new HashMap<>();

    /**
     * 一致性hash算法优势在于 新增删除节点，原始数据迁移较少
     */
    @Test
    public void testCompareMoveData() {
        for (ShardingAlgorithmEnum func : ShardingAlgorithmEnum.values()) {
            log.info("start: func={}", func);
            this.userMap = new HashMap<>();
            for (int i = 2; i < 30; i += 3) {
                calculatePerIndex(func, i, (int) Math.pow(2, i) * 20);
//                calculatePerIndex(func, i, i * 3);
            }
        }
    }

    private void calculatePerIndex(ShardingAlgorithmEnum func, int totalSlice, int totalData) {
        Map<Integer, String> newUserMap = new HashMap<>();
        for (int i = 0; i < totalData; i++) {
            String finalTable = func.getFunc().apply((long) i, totalSlice);
//            System.out.println(i + "  " + finalTable);
            newUserMap.put(i, finalTable);
        }
        long changCount = userMap.entrySet().stream().filter(v -> {
            String newIndex = newUserMap.get(v.getKey());
            return !Objects.equals(newIndex, v.getValue());
        }).count();

        this.userMap = newUserMap;
        Map<String, List<Map.Entry<Integer, String>>> tableMap = userMap.entrySet()
                .stream().collect(Collectors.groupingBy(Map.Entry::getValue));

        AtomicInteger counter = new AtomicInteger();
        tableMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()).forEach(e -> {
            counter.addAndGet(e.getValue().size());
            System.out.println(e.getKey() + ":" + e.getValue().size());
        });
        log.info("{}: count={} changeCount={}", func.name(), counter.get(), changCount);
    }
}
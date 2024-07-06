package com.github.kuangcp.situation.commoncode;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2022-06-30 10:07
 */
@Slf4j
public class CommonRelationTest {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Parts {
        // 用于equal函数
        private String id;
        private String parts;
        private String first;
        private String second;

        public Parts(String parts, String first, String second) {
            this.parts = parts;
            this.first = first;
            this.second = second;
        }
    }

    int partsSize = 30;
    int poolSize = 50;

    int loopCount = 0;

    @Test
    public void testGenData() throws IOException {
        List<Parts> parts = initData();
        Gson gson = new Gson();
        String s = gson.toJson(parts);
        Files.write(Paths.get("b.json"), s.getBytes(StandardCharsets.UTF_8));
    }
    // -ea -Xmx500m  -XX:+UseG1GC -XX:+UseStringDeduplication -XX:+PrintStringDeduplicationStatistics -XX:+PrintStringTableStatistics

    /**
     * 500000 500000 申请了 100M 内存
     * <p> running time (millis) = 3629
     * -----------------------------------------
     * ms     %     Task name
     * -----------------------------------------
     * 00620  017%  init
     * 00692  019%  cache
     * 02317  064%  merge
     */
    @Test
    public void testMergeCodeMap() throws IOException, InterruptedException {
        TimeUnit.SECONDS.sleep(6);
        List<Parts> parts = readData();

        for (int i = 0; i < 100; i++) {
            Map<String, Set<String>> result = loopCodeMap(parts);
            log.info("finish {}", i);
            TimeUnit.SECONDS.sleep(5);
        }

        TimeUnit.SECONDS.sleep(100000);
    }

    @Test
    public void testMergeCodeMapValid() throws InterruptedException {
        List<Parts> parts = new ArrayList<>();
        parts.add(new Parts("A", "B", "C"));
        parts.add(new Parts("B", "ii", "X"));
        parts.add(new Parts("X", "Y", "P"));
        parts.add(new Parts("P", "Xx", "C1"));
        parts.add(new Parts("Xx", "uu", "C2"));
        parts.add(new Parts("uu", "uuw", "C3"));

        parts.add(new Parts("F", "I", "O"));

        parts.add(new Parts("1", "11", ""));

        parts.add(new Parts("x", " ", null));
        parts.add(new Parts(" ", "uu", null));

        parts.add(new Parts("22", "2", "222"));

        parts.add(new Parts("y", "c", null));
        parts.add(new Parts("y", "a", null));
        parts.add(new Parts("a", null, "b"));


        log.info("start");
        Map<String, Set<String>> result = loopCodeMap(parts);
        result.forEach((k, v) -> log.info("{} {}", k, v));

        Set<String> s = new HashSet<>();
        s.add("F,I,O");
        s.add("2,22,222");
        s.add("1,11");
        s.add("a,b,c,y");
        s.add("A,B,C,C1,C2,C3,P,X,Xx,Y,ii,uu,uuw");
        assertResultMap(s, result);
    }

    @Test
    public void testMergeSet() throws IOException {
        List<Parts> parts = readData();

        Set<Set<String>> cache = new HashSet<>();
        for (Parts part : parts) {
            cache.add(new HashSet<>(Arrays.asList(part.getParts(), part.getFirst(), part.getSecond())));
        }

        log.info("before: {}", cache.size());
//        log.info("{}", cache);
        int i = 0;
        while (mergeWithForEach(cache)) {
            i++;
            log.info("merge loop:{} size：{}", loopCount, cache.size());
        }
        log.info("result: merge:{} size:{}", i, cache.size());
        log.info("{}", cache);
    }


    private String randStr(List<String> pool) {
        return pool.get(new Random().nextInt(poolSize));
    }

    private List<Parts> initData() {
        List<String> pool = IntStream.range(0, poolSize)
                .mapToObj(v -> UUID.randomUUID().toString().substring(0, 8))
                .collect(Collectors.toList());

        List<Parts> parts = new ArrayList<>();
        for (int i = 0; i < partsSize; i++) {
            Parts p = new Parts(System.nanoTime() + "" + i,
                    randStr(pool),
                    randStr(pool),
                    randStr(pool));
            parts.add(p);
        }
        return parts;
    }

    private List<Parts> readData() throws IOException {
        Path path = Paths.get("30w.json");
        byte[] bytes = Files.readAllBytes(path);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Parts>>() {
        }.getType();
        return gson.fromJson(new String(bytes), type);
    }

    private void appendParts(Map<String, Set<String>> cache, String parts, Set<String> tmp) {
        Set<String> exist = cache.get(parts);
        if (Objects.nonNull(exist)) {
            exist.addAll(tmp);
        } else {
            cache.put(parts, tmp);
        }
    }

    private Map<String, Set<String>> loopCodeMap(List<Parts> parts) throws InterruptedException {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start("cache");
        Map<String, Set<String>> cache = new HashMap<>(partsSize * 3);
        for (Parts part : parts) {
            Set<String> tmp = Stream.of(part.getParts(), part.getFirst(), part.getSecond())
                    .filter(StringUtils::isNoneBlank).collect(Collectors.toSet());
            if (tmp.size() == 1) {
                continue;
            }
            appendParts(cache, part.getParts(), tmp);
            appendParts(cache, part.getFirst(), tmp);
            appendParts(cache, part.getSecond(), tmp);
        }
        stopWatch.stop();

        Map<String, Set<String>> result = mergeWithMain(stopWatch, cache);

        int i = 0;
        int pc = 0;
        for (Map.Entry<String, Set<String>> entry : result.entrySet()) {
            int size = entry.getValue().size();
            pc += size;
            if (size > 3) {
                i++;
//                System.out.print(size + ",");
            }
        }

        log.info("avg={}", pc / result.size());

        log.info("配件数{} 去重总编码数 {} 通用码块={} 块内编码数 {} 合并次数={} \n{}",
                parts.size(), cache.size(), result.size(), pc, i, stopWatch.prettyPrint());
//        log.info(": result={}", result);

        return result;
    }

    private Map<String, Set<String>> mergeWithMain(StopWatch stopWatch, Map<String, Set<String>> cache) throws InterruptedException {
        stopWatch.start("merge");
        Map<String, Set<String>> result = new HashMap<>();
        Map<String, String> handled = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : cache.entrySet()) {
            String code = entry.getKey();
            if (handled.containsKey(code)) {
                continue;
            }

            Set<String> total = new HashSet<>();
            loopFind(cache, total, handled, code);
            result.put(UUID.randomUUID().toString(), total);
        }
        stopWatch.stop();
        return result;
    }

    /**
     * 引入线程池，但是数据集太分散 锁的调度开销 远大于多层循环耗时 最终执行时间翻了三倍,内存压力也很大，
     * 而且从业务逻辑上并行可能导致本应被关联的数据没有关联上 遍历数据是具有顺序性要求的
     *
     * @see CommonRelationTest#mergeWithMain(StopWatch, Map)
     */
    @Deprecated
    private Map<String, Set<String>> mergeWithPool(StopWatch stopWatch, Map<String, Set<String>> cache) throws InterruptedException {
        stopWatch.start("merge");
        ExecutorService pool = Executors.newFixedThreadPool(7);
        CountDownLatch latch = new CountDownLatch(cache.entrySet().size());
        Map<String, Set<String>> result = new ConcurrentHashMap<>();
        Map<String, String> handled = new ConcurrentHashMap<>();
        for (Map.Entry<String, Set<String>> entry : cache.entrySet()) {
            String code = entry.getKey();
            if (Objects.isNull(code) || handled.containsKey(code)) {
                latch.countDown();
                continue;
            }

            pool.execute(() -> {
                Set<String> total = new HashSet<>();
                loopFind(cache, total, handled, code);
                result.put(UUID.randomUUID().toString(), total);
                latch.countDown();
            });
        }
        latch.await();
        stopWatch.stop();
        return result;
    }

    private void assertResultMap(Set<String> exceptMap, Map<String, Set<String>> result) {
        Map<String, String> sortMap = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : result.entrySet()) {
            String key = entry.getKey();
            sortMap.put(key, entry.getValue().stream().filter(StringUtils::isNoneBlank)
                    .sorted().collect(Collectors.joining(",")));
        }
        HashSet<String> sameSet = new HashSet<>();
        sameSet.addAll(exceptMap);
        sameSet.retainAll(sortMap.values());

        HashSet<String> re = new HashSet<>();

        re.addAll(exceptMap);
        re.addAll(sortMap.values());
        re.removeAll(sameSet);

        if (!re.isEmpty()) {
            log.info("re={}", re);
            Assert.fail();
        }
    }

    private void loopFind(Map<String, Set<String>> cache, Set<String> total, Map<String, String> handled, String code) {
        total.add(code);
        handled.put(code, "");
        Set<String> next = cache.get(code);

        Set<String> appendSet = new HashSet<>();
        Set<String> loopSet = new HashSet<>();
        Set<String> swapTmp;
        while (CollectionUtils.isNotEmpty(next)) {
            swapTmp = appendSet;
            appendSet = loopSet;
            loopSet = swapTmp;

            appendSet.clear();
            for (String s : next) {
                Set<String> sNext = cache.get(s);
                if (CollectionUtils.isNotEmpty(sNext)) {
//                    log.info("{} code={}", total.size(), code);
                    sNext.stream().filter(v -> !total.contains(v)).forEach(appendSet::add);
                }
                total.add(s);
                handled.put(s, "");
            }

            next = appendSet;
        }
    }

    /**
     * 当数据集中出现关联的数据较多时，栈的深度会爆炸
     *
     * @see CommonRelationTest#loopFind
     */
    @Deprecated
    private void recursiveFind(Map<String, Set<String>> cache, Set<String> total, String code) {
//        log.info("code={}", code);
        total.add(code);
        Set<String> smallBlock = cache.get(code);
        if (CollectionUtils.isEmpty(smallBlock)) {
            return;
        }

        log.info("{} code={} {}", total.size(), code, smallBlock);

        smallBlock.stream()
                .filter(v -> !total.contains(v))
                .forEach(v -> recursiveFind(cache, total, v));
    }

    /**
     * 双重循环，衰减找出关联数据 O(N2)
     */
    private boolean mergeWithForEach(Set<Set<String>> cache) {
        boolean hasSame = false;

        for (Set<String> sets : cache) {
            for (Set<String> tmp : cache) {
                loopCount++;
                if (Objects.equals(sets, tmp)) {
                    continue;
                }
                hasSame = tmp.stream().anyMatch(sets::contains);
                if (hasSame) {
//                    log.info("{} & {}", sets, tmp);
                    sets.addAll(tmp);
                    cache.remove(tmp);
                    break;
                }
            }
            if (hasSame) {
                break;
            }
        }

        return hasSame;
    }


}

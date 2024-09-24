package com.github.kuangcp.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Kuangcp
 * 2024-03-29 14:12
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 1, time = 1)
@Threads(2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class StrUtilTest {

    //    @Benchmark
    public void randList() {
        StrUtil.randomAlpha(32);
    }

    // arrayList 本身是对数组的封装，性能影响不大
    @Benchmark
    public void randArray() {
        StrUtil.randomAlphaA(32);
    }

    // 省去对象创建，效率更好
//    @Benchmark
    public void randArrayLocal() {
        StrUtil.randomAlphaAL(32);
    }

    @Benchmark
    public void randStrLocal() {
        StrUtil.randomAlphaStrL(32);
    }

    @Benchmark
    public void randCharLocal() {
        StrUtil.randomAlphaCharCalL(32);
    }

    @Benchmark
    public void randCharLocal2() {
        StrUtil.randomAlphaCharL(32);
    }

    //    @Benchmark
    public void randAsciiLocal() {
        StrUtil.randomAsciiL(32);
    }

    //    @Benchmark
    public void randAsciiStrLocal() {
        StrUtil.randomAsciiStrL(32);
    }

    @Test
    public void testBenchmark() throws Exception {
        String file = "/tmp/jmh-" + this.getClass().getSimpleName() + System.currentTimeMillis() + ".log";
        Options options = new OptionsBuilder()
                .include(this.getClass().getSimpleName())
                .output(file).build();
        System.out.println(file);
        new Runner(options).run();
    }

    @Test
    public void testAsciiL() throws Exception {
        for (int i = 0; i < 10; i++) {
            System.out.println(StrUtil.randomAsciiL(10));
        }
    }

    @Test
    public void testDiff() throws Exception {
        System.out.println(disCount(StrUtil::randomAlpha));
        System.out.println(disCount(StrUtil::randomAlphaA));
        System.out.println(disCount(StrUtil::randomAlphaAS));
        System.out.println(disCount(StrUtil::randomAlphaAL));
        System.out.println(disCount(StrUtil::randomAlphaStrL));
    }

    @Test
    public void testCompare() throws Exception {
        Map<String, Integer> win = new HashMap<>();
        int loop = 2000;
        for (int i = 0; i < loop; i++) {
            Map<String, Integer> tmp = new HashMap<>();
            tmp.put("a", disCount(StrUtil::randomAlpha));
            tmp.put("b", disCount(StrUtil::randomAlphaA));
            tmp.put("c", disCount(StrUtil::randomAlphaAS));
            tmp.put("d", disCount(StrUtil::randomAlphaAL));
            tmp.put("e", disCount(StrUtil::randomAlphaStrL));
            tmp.put("f", disCount(StrUtil::randomAlphaNumStrL));
            tmp.put("g", disCount(StrUtil::randomAsciiL));
            tmp.put("h", disCount(RandomStringUtils::randomAscii));
            tmp.put("z", disCount(a -> UUID.randomUUID().toString()));
            List<Map.Entry<String, Integer>> top = tmp.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList());

            for (int size = top.size(); size > 0; size--) {
                String key = top.get(size - 1).getKey();
                Integer last = win.getOrDefault(key, 0);
                win.put(key, last + size);
            }
        }
        int sum = 0;
        for (int i = 0; i < win.size(); i++) {
            sum += i + 1;
        }
        sum *= loop;
        int finalSum = sum;
        AtomicInteger cnt = new AtomicInteger();
        win.forEach((a, b) -> System.out.println(cnt.incrementAndGet() + a + " " + b + " " + b / (float) finalSum));
    }

    private static int disCount(Function<Integer, String> func) {
        Set<String> re3 = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            re3.add(func.apply(8));
        }
        return re3.size();
    }

    @Test
    public void testAsc() throws Exception {
        for (int i = 32; i < 127; i++) {
            System.out.print((char) i);
        }
        System.out.println();

        long start = System.currentTimeMillis();
        int initialCapacity = 300000;
        List<String> x1 = new ArrayList<>(initialCapacity);
        for (int i = 0; i < initialCapacity; i++) {
            char[] tmp = new char[6];
            for (int j = 0; j < tmp.length; j++) {
                tmp[j] = (char) ('a' + j);
            }
            x1.add(new String(tmp));
        }
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        List<String> x2 = new ArrayList<>(initialCapacity);
        for (int i = 0; i < initialCapacity; i++) {
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < 7; j++) {
                s.append((char) ('a' + j));
            }
            x2.add(s.toString());
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}

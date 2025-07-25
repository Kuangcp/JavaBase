package com.github.kuangcp.random;

import org.junit.Test;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * 全面对比 Random 和 ThreadLocalRandom 在各种场景下的性能差异和特性
 *
 * @author kuangcp
 */
public class RandomCompareTest {

    private static final int LOOP_COUNT = 1000000;
    private static final int THREAD_COUNT = 4;
    private static final int WARM_UP_COUNT = 1000;
    private static final String CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    // 用于统计的数据结构
    private static class Statistics {
        long minTime = Long.MAX_VALUE;
        long maxTime = Long.MIN_VALUE;
        long totalTime = 0;
        long count = 0;
        List<Long> allTimes = new ArrayList<>();

        void addTime(long time) {
            minTime = Math.min(minTime, time);
            maxTime = Math.max(maxTime, time);
            totalTime += time;
            count++;
            allTimes.add(time);
        }

        double getAverage() {
            return count > 0 ? (double) totalTime / count : 0;
        }

        double getStandardDeviation() {
            if (count < 2) return 0;
            double mean = getAverage();
            double variance = allTimes.stream()
                    .mapToDouble(time -> time - mean)
                    .map(diff -> diff * diff)
                    .average()
                    .orElse(0.0);
            return Math.sqrt(variance);
        }

        double getMedian() {
            if (allTimes.isEmpty()) return 0;
            List<Long> sorted = new ArrayList<>(allTimes);
            Collections.sort(sorted);
            int middle = sorted.size() / 2;
            if (sorted.size() % 2 == 0) {
                return (sorted.get(middle - 1) + sorted.get(middle)) / 2.0;
            } else {
                return sorted.get(middle);
            }
        }

        @Override
        public String toString() {
            return String.format("min=%d ns, max=%d ns, avg=%.2f ns, median=%.2f ns, stddev=%.2f ns, count=%d",
                    minTime, maxTime, getAverage(), getMedian(), getStandardDeviation(), count);
        }
    }

    // 预热JVM
    private void warmup() {
        Random random = new Random();
        for (int i = 0; i < WARM_UP_COUNT; i++) {
            random.nextInt();
            ThreadLocalRandom.current().nextInt();
        }
    }

    @Test
    public void testSingleThread() {
        warmup();

        // 测试 Random
        long startRandom = System.nanoTime();
        Random random = new Random();
        for (int i = 0; i < LOOP_COUNT; i++) {
            random.nextInt(100);
        }
        long randomTime = System.nanoTime() - startRandom;

        // 测试 ThreadLocalRandom
        long startThreadLocal = System.nanoTime();
        for (int i = 0; i < LOOP_COUNT; i++) {
            ThreadLocalRandom.current().nextInt(100);
        }
        long threadLocalTime = System.nanoTime() - startThreadLocal;

        System.out.printf("Single Thread Performance:%n");
        System.out.printf("Random time: %d ns%n", randomTime);
        System.out.printf("ThreadLocalRandom time: %d ns%n", threadLocalTime);
    }

    @Test
    public void testMultiThread() throws InterruptedException {
        warmup();
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        List<Long> randomTimes = new ArrayList<>();
        List<Long> threadLocalTimes = new ArrayList<>();

        // 测试共享 Random 实例
        Random sharedRandom = new Random();
        for (int t = 0; t < THREAD_COUNT; t++) {
            CountDownLatch finalLatch = latch;
            executor.submit(() -> {
                long start = System.nanoTime();
                for (int i = 0; i < LOOP_COUNT; i++) {
                    sharedRandom.nextInt(100);
                }
                randomTimes.add(System.nanoTime() - start);
                finalLatch.countDown();
            });
        }
        latch.await();

        // 重置 CountDownLatch
        latch = new CountDownLatch(THREAD_COUNT);

        // 测试 ThreadLocalRandom
        for (int t = 0; t < THREAD_COUNT; t++) {
            CountDownLatch finalLatch1 = latch;
            executor.submit(() -> {
                long start = System.nanoTime();
                for (int i = 0; i < LOOP_COUNT; i++) {
                    ThreadLocalRandom.current().nextInt(100);
                }
                threadLocalTimes.add(System.nanoTime() - start);
                finalLatch1.countDown();
            });
        }
        latch.await();

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // 计算并打印结果
        long avgRandomTime = randomTimes.stream().mapToLong(Long::longValue).sum() / THREAD_COUNT;
        long avgThreadLocalTime = threadLocalTimes.stream().mapToLong(Long::longValue).sum() / THREAD_COUNT;

        System.out.printf("Multi Thread Performance (average per thread):%n");
        System.out.printf("Shared Random time: %d ns%n", avgRandomTime);
        System.out.printf("ThreadLocalRandom time: %d ns%n", avgThreadLocalTime);
    }

    @Test
    public void testDifferentRanges() {
        int[] ranges = {10, 100, 1000, 10000, 100000, Integer.MAX_VALUE};
        Random random = new Random();

        for (int range : ranges) {
            Statistics randomStats = new Statistics();
            Statistics threadLocalStats = new Statistics();

            for (int i = 0; i < LOOP_COUNT; i++) {
                // Test Random
                long start = System.nanoTime();
                random.nextInt(range);
                randomStats.addTime(System.nanoTime() - start);

                // Test ThreadLocalRandom
                start = System.nanoTime();
                ThreadLocalRandom.current().nextInt(range);
                threadLocalStats.addTime(System.nanoTime() - start);
            }

            System.out.printf("Range [0-%d]:%n", range);
            System.out.printf("Random: %s%n", randomStats);
            System.out.printf("ThreadLocalRandom: %s%n%n", threadLocalStats);
        }
    }

    @Test
    public void testDifferentTypes() {
        Random random = new Random();
        Statistics[] randomStats = new Statistics[5];
        Statistics[] threadLocalStats = new Statistics[5];

        for (int i = 0; i < 5; i++) {
            randomStats[i] = new Statistics();
            threadLocalStats[i] = new Statistics();
        }

        for (int i = 0; i < LOOP_COUNT; i++) {
            // Test int
            long start = System.nanoTime();
            random.nextInt();
            randomStats[0].addTime(System.nanoTime() - start);

            start = System.nanoTime();
            ThreadLocalRandom.current().nextInt();
            threadLocalStats[0].addTime(System.nanoTime() - start);

            // Test long
            start = System.nanoTime();
            random.nextLong();
            randomStats[1].addTime(System.nanoTime() - start);

            start = System.nanoTime();
            ThreadLocalRandom.current().nextLong();
            threadLocalStats[1].addTime(System.nanoTime() - start);

            // Test double
            start = System.nanoTime();
            random.nextDouble();
            randomStats[2].addTime(System.nanoTime() - start);

            start = System.nanoTime();
            ThreadLocalRandom.current().nextDouble();
            threadLocalStats[2].addTime(System.nanoTime() - start);

            // Test boolean
            start = System.nanoTime();
            random.nextBoolean();
            randomStats[3].addTime(System.nanoTime() - start);

            start = System.nanoTime();
            ThreadLocalRandom.current().nextBoolean();
            threadLocalStats[3].addTime(System.nanoTime() - start);

            // Test float
            start = System.nanoTime();
            random.nextFloat();
            randomStats[4].addTime(System.nanoTime() - start);

            start = System.nanoTime();
            ThreadLocalRandom.current().nextFloat();
            threadLocalStats[4].addTime(System.nanoTime() - start);
        }

        String[] types = {"Integer", "Long", "Double", "Boolean", "Float"};
        for (int i = 0; i < types.length; i++) {
            System.out.printf("%s:%n", types[i]);
            System.out.printf("Random: %s%n", randomStats[i]);
            System.out.printf("ThreadLocalRandom: %s%n%n", threadLocalStats[i]);
        }
    }

    @Test
    public void testThreadScalability() throws InterruptedException {
        int[] threadCounts = {1, 2, 4, 8, 16, 32};

        for (int threadCount : threadCounts) {
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);
            List<Long> randomTimes = new ArrayList<>();
            List<Long> threadLocalTimes = new ArrayList<>();

            // Test shared Random
            Random sharedRandom = new Random();
            for (int t = 0; t < threadCount; t++) {
                executor.submit(() -> {
                    long start = System.nanoTime();
                    for (int i = 0; i < LOOP_COUNT; i++) {
                        sharedRandom.nextInt(100);
                    }
                    randomTimes.add(System.nanoTime() - start);
                    latch.countDown();
                });
            }
            latch.await();

            // Reset latch
            CountDownLatch latch2 = new CountDownLatch(threadCount);

            // Test ThreadLocalRandom
            for (int t = 0; t < threadCount; t++) {
                executor.submit(() -> {
                    long start = System.nanoTime();
                    for (int i = 0; i < LOOP_COUNT; i++) {
                        ThreadLocalRandom.current().nextInt(100);
                    }
                    threadLocalTimes.add(System.nanoTime() - start);
                    latch2.countDown();
                });
            }
            latch2.await();

            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);

            double avgRandomTime = randomTimes.stream().mapToLong(Long::longValue).average().orElse(0);
            double avgThreadLocalTime = threadLocalTimes.stream().mapToLong(Long::longValue).average().orElse(0);

            System.out.printf("Thread Count: %d%n", threadCount);
            System.out.printf("Average Random time: %.2f ns%n", avgRandomTime);
            System.out.printf("Average ThreadLocalRandom time: %.2f ns%n%n", avgThreadLocalTime);
        }
    }

    @Test
    public void testDistribution() {
        int bucketCount = 10;
        int[] randomBuckets = new int[bucketCount];
        int[] threadLocalBuckets = new int[bucketCount];

        Random random = new Random();

        for (int i = 0; i < LOOP_COUNT; i++) {
            int randomValue = random.nextInt(bucketCount);
            int threadLocalValue = ThreadLocalRandom.current().nextInt(bucketCount);

            randomBuckets[randomValue]++;
            threadLocalBuckets[threadLocalValue]++;
        }

        System.out.println("Distribution Analysis:");
        System.out.println("Random distribution:");
        printDistribution(randomBuckets);
        System.out.println("\nThreadLocalRandom distribution:");
        printDistribution(threadLocalBuckets);

        // Chi-square test for uniformity
        double randomChiSquare = calculateChiSquare(randomBuckets);
        double threadLocalChiSquare = calculateChiSquare(threadLocalBuckets);

        System.out.printf("%nChi-square test results:%n");
        System.out.printf("Random: %.2f%n", randomChiSquare);
        System.out.printf("ThreadLocalRandom: %.2f%n", threadLocalChiSquare);
    }

    private void printDistribution(int[] buckets) {
        int total = Arrays.stream(buckets).sum();
        for (int i = 0; i < buckets.length; i++) {
            double percentage = (double) buckets[i] / total * 100;
            System.out.printf("Bucket %d: %d (%.2f%%)%n", i, buckets[i], percentage);
        }
    }

    private double calculateChiSquare(int[] buckets) {
        double expected = Arrays.stream(buckets).sum() / (double) buckets.length;
        return Arrays.stream(buckets)
                .mapToDouble(observed -> Math.pow(observed - expected, 2) / expected)
                .sum();
    }

    @Test
    public void testRandomString() {
        int[] lengths = {5, 10, 20, 50, 100};
        Random random = new Random();

        for (int length : lengths) {
            Statistics randomStats = new Statistics();
            Statistics threadLocalStats = new Statistics();

            for (int i = 0; i < LOOP_COUNT / 100; i++) {  // Reduced iterations due to string operations
                // Test Random
                long start = System.nanoTime();
                generateRandomString(random, length);
                randomStats.addTime(System.nanoTime() - start);

                // Test ThreadLocalRandom
                start = System.nanoTime();
                generateRandomString(ThreadLocalRandom.current(), length);
                threadLocalStats.addTime(System.nanoTime() - start);
            }

            System.out.printf("String length: %d%n", length);
            System.out.printf("Random: %s%n", randomStats);
            System.out.printf("ThreadLocalRandom: %s%n%n", threadLocalStats);
        }
    }

    private String generateRandomString(Random random, int length) {
        return random.ints(length, 0, 36)
                .mapToObj(i -> Character.toString("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(i)))
                .collect(Collectors.joining());
    }

    private String generateRandomString(ThreadLocalRandom random, int length) {
        return random.ints(length, 0, 36)
                .mapToObj(i -> Character.toString("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(i)))
                .collect(Collectors.joining());
    }

    @Test
    public void testRandomArray() {
        int[] sizes = {100, 1000, 10000};
        Random random = new Random();

        for (int size : sizes) {
            Statistics randomStats = new Statistics();
            Statistics threadLocalStats = new Statistics();

            for (int i = 0; i < LOOP_COUNT / 1000; i++) {  // Reduced iterations due to array operations
                // Test Random
                long start = System.nanoTime();
                int[] randomArray = generateRandomArray(random, size);
                randomStats.addTime(System.nanoTime() - start);

                // Test ThreadLocalRandom
                start = System.nanoTime();
                int[] threadLocalArray = generateRandomArray(ThreadLocalRandom.current(), size);
                threadLocalStats.addTime(System.nanoTime() - start);

                // Verify arrays are properly filled
                assertTrue(Arrays.stream(randomArray).anyMatch(x -> x != 0));
                assertTrue(Arrays.stream(threadLocalArray).anyMatch(x -> x != 0));
            }

            System.out.printf("Array size: %d%n", size);
            System.out.printf("Random: %s%n", randomStats);
            System.out.printf("ThreadLocalRandom: %s%n%n", threadLocalStats);
        }
    }

    private int[] generateRandomArray(Random random, int size) {
        return random.ints(size).toArray();
    }

    private int[] generateRandomArray(ThreadLocalRandom random, int size) {
        return random.ints(size).toArray();
    }

    @Test
    public void testGaussianDistribution() {
        int bucketCount = 20;
        int[] randomBuckets = new int[bucketCount];
        int[] threadLocalBuckets = new int[bucketCount];

        Random random = new Random();

        for (int i = 0; i < LOOP_COUNT; i++) {
            double randomGaussian = random.nextGaussian();
            double threadLocalGaussian = ThreadLocalRandom.current().nextGaussian();

            int randomBucket = getBucket(randomGaussian, bucketCount);
            int threadLocalBucket = getBucket(threadLocalGaussian, bucketCount);

            if (randomBucket >= 0 && randomBucket < bucketCount) {
                randomBuckets[randomBucket]++;
            }
            if (threadLocalBucket >= 0 && threadLocalBucket < bucketCount) {
                threadLocalBuckets[threadLocalBucket]++;
            }
        }

        System.out.println("Gaussian Distribution Analysis:");
        System.out.println("Random Gaussian distribution:");
        printDistribution(randomBuckets);
        System.out.println("\nThreadLocalRandom Gaussian distribution:");
        printDistribution(threadLocalBuckets);
    }

    private int getBucket(double gaussian, int bucketCount) {
        // Map the gaussian value (-3 to 3 standard deviations) to a bucket
        double normalized = (gaussian + 3.0) / 6.0;  // Map to 0-1 range
        return (int) (normalized * bucketCount);
    }

    @Test
    public void testBoundaryValues() {
        Random random = new Random();
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

        // Test various boundary conditions
        int[] boundaries = {
                Integer.MAX_VALUE,
                Integer.MAX_VALUE - 1,
                Integer.MIN_VALUE,
                Integer.MIN_VALUE + 1,
                0,
                1,
                -1
        };

        for (int boundary : boundaries) {
            try {
                random.nextInt(boundary);
                System.out.printf("Random.nextInt(%d) succeeded%n", boundary);
            } catch (Exception e) {
                System.out.printf("Random.nextInt(%d) threw %s%n", boundary, e.getClass().getSimpleName());
            }

            try {
                threadLocalRandom.nextInt(boundary);
                System.out.printf("ThreadLocalRandom.nextInt(%d) succeeded%n", boundary);
            } catch (Exception e) {
                System.out.printf("ThreadLocalRandom.nextInt(%d) threw %s%n", boundary, e.getClass().getSimpleName());
            }
        }
    }

    @Test
    public void testSequentialGeneration() {
        int sequenceLength = 1000;
        Random random = new Random();

        // Generate sequences
        int[] randomSequence = new int[sequenceLength];
        int[] threadLocalSequence = new int[sequenceLength];

        for (int i = 0; i < sequenceLength; i++) {
            randomSequence[i] = random.nextInt(100);
            threadLocalSequence[i] = ThreadLocalRandom.current().nextInt(100);
        }

        // Analyze sequences for patterns
        analyzeSequence("Random", randomSequence);
        analyzeSequence("ThreadLocalRandom", threadLocalSequence);
    }

    private void analyzeSequence(String name, int[] sequence) {
        // Calculate basic statistics
        IntSummaryStatistics stats = Arrays.stream(sequence).summaryStatistics();

        // Calculate runs (consecutive increasing/decreasing numbers)
        int increasingRuns = 0;
        int decreasingRuns = 0;
        int currentRun = 1;
        boolean wasIncreasing = true;

        for (int i = 1; i < sequence.length; i++) {
            if (sequence[i] > sequence[i - 1]) {
                if (wasIncreasing) {
                    currentRun++;
                } else {
                    if (currentRun > 1) decreasingRuns++;
                    currentRun = 1;
                    wasIncreasing = true;
                }
            } else if (sequence[i] < sequence[i - 1]) {
                if (!wasIncreasing) {
                    currentRun++;
                } else {
                    if (currentRun > 1) increasingRuns++;
                    currentRun = 1;
                    wasIncreasing = false;
                }
            }
        }

        System.out.printf("%s Sequence Analysis:%n", name);
        System.out.printf("Min: %d, Max: %d, Average: %.2f%n",
                stats.getMin(), stats.getMax(), stats.getAverage());
        System.out.printf("Increasing runs: %d, Decreasing runs: %d%n%n",
                increasingRuns, decreasingRuns);
    }

    @Test
    public void testSeedReproducibility() {
        long seed = 12345L;
        Random random1 = new Random(seed);
        Random random2 = new Random(seed);
        ThreadLocalRandom tlRandom = ThreadLocalRandom.current();

        int count = 1000;
        boolean allMatch = true;
        List<Integer> sequence1 = new ArrayList<>();
        List<Integer> sequence2 = new ArrayList<>();

        // 测试Random的种子复现性
        for (int i = 0; i < count; i++) {
            sequence1.add(random1.nextInt());
            sequence2.add(random2.nextInt());
        }

        assertEquals("使用相同种子的Random应产生相同序列", sequence1, sequence2);

        // 记录ThreadLocalRandom的序列
        List<Integer> tlSequence = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tlSequence.add(tlRandom.nextInt());
        }

        // 验证ThreadLocalRandom序列与Random序列的不同
        assertNotEquals("ThreadLocalRandom序列应与Random序列不同", sequence1, tlSequence);
    }

    @Test
    public void testStreamPerformance() {
        int streamSize = 1000000;
        Statistics randomStats = new Statistics();
        Statistics threadLocalStats = new Statistics();
        Statistics parallelRandomStats = new Statistics();
        Statistics parallelThreadLocalStats = new Statistics();

        // 测试串行流
        for (int i = 0; i < 10; i++) {
            // Random串行流
            long start = System.nanoTime();
            Random random = new Random();
            random.ints(streamSize).forEach(x -> {
            });
            randomStats.addTime(System.nanoTime() - start);

            // ThreadLocalRandom串行流
            start = System.nanoTime();
            ThreadLocalRandom.current().ints(streamSize).forEach(x -> {
            });
            threadLocalStats.addTime(System.nanoTime() - start);

            // Random并行流
            start = System.nanoTime();
            random.ints(streamSize).parallel().forEach(x -> {
            });
            parallelRandomStats.addTime(System.nanoTime() - start);

            // ThreadLocalRandom并行流
            start = System.nanoTime();
            ThreadLocalRandom.current().ints(streamSize).parallel().forEach(x -> {
            });
            parallelThreadLocalStats.addTime(System.nanoTime() - start);
        }

        System.out.println("Stream Performance Test Results:");
        System.out.printf("Random (Serial): %s%n", randomStats);
        System.out.printf("ThreadLocalRandom (Serial): %s%n", threadLocalStats);
        System.out.printf("Random (Parallel): %s%n", parallelRandomStats);
        System.out.printf("ThreadLocalRandom (Parallel): %s%n", parallelThreadLocalStats);
    }

    @Test
    public void testMemoryUsage() throws InterruptedException {
        int iterations = 1000000;
        System.gc(); // 尝试在测试前清理内存
        Thread.sleep(1000); // 等待GC完成

        long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        // 测试Random
        List<Random> randoms = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            randoms.add(new Random());
        }
        long randomMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() - startMemory;
        randoms = null;

        System.gc();
        Thread.sleep(1000);

        // 测试ThreadLocalRandom
        startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        List<ThreadLocalRandom> threadLocals = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            threadLocals.add(ThreadLocalRandom.current());
        }
        long threadLocalMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() - startMemory;

        System.out.printf("Memory Usage Test Results:%n");
        System.out.printf("Random instances memory: %d bytes%n", randomMemory);
        System.out.printf("ThreadLocalRandom instances memory: %d bytes%n", threadLocalMemory);
    }

    @Test
    public void testSecureRandomComparison() {
        int iterations = 10000;
        Statistics randomStats = new Statistics();
        Statistics threadLocalStats = new Statistics();
        Statistics secureRandomStats = new Statistics();

        Random random = new Random();
        ThreadLocalRandom threadLocal = ThreadLocalRandom.current();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < iterations; i++) {
            // Test Random
            long start = System.nanoTime();
            random.nextBytes(new byte[16]);
            randomStats.addTime(System.nanoTime() - start);

            // Test ThreadLocalRandom
            start = System.nanoTime();
            threadLocal.nextBytes(new byte[16]);
            threadLocalStats.addTime(System.nanoTime() - start);

            // Test SecureRandom
            start = System.nanoTime();
            secureRandom.nextBytes(new byte[16]);
            secureRandomStats.addTime(System.nanoTime() - start);
        }

        System.out.println("Secure Random Comparison Results:");
        System.out.printf("Random: %s%n", randomStats);
        System.out.printf("ThreadLocalRandom: %s%n", threadLocalStats);
        System.out.printf("SecureRandom: %s%n", secureRandomStats);
    }

    @Test
    public void testRangeDistribution() {
        int iterations = 1000000;
        int[] ranges = {10, 100, 1000};

        for (int range : ranges) {
            Map<Integer, Integer> randomDist = new HashMap<>();
            Map<Integer, Integer> threadLocalDist = new HashMap<>();

            Random random = new Random();
            ThreadLocalRandom threadLocal = ThreadLocalRandom.current();

            // 生成随机数并统计分布
            for (int i = 0; i < iterations; i++) {
                int randomNum = random.nextInt(range);
                int threadLocalNum = threadLocal.nextInt(range);

                randomDist.merge(randomNum, 1, Integer::sum);
                threadLocalDist.merge(threadLocalNum, 1, Integer::sum);
            }

            // 计算分布均匀性
            double expectedFreq = (double) iterations / range;
            double randomChiSquare = calculateChiSquare(randomDist.values(), expectedFreq);
            double threadLocalChiSquare = calculateChiSquare(threadLocalDist.values(), expectedFreq);

            System.out.printf("Range [0-%d] Distribution Analysis:%n", range);
            System.out.printf("Random Chi-square: %.2f%n", randomChiSquare);
            System.out.printf("ThreadLocalRandom Chi-square: %.2f%n%n", threadLocalChiSquare);
        }
    }

    private double calculateChiSquare(Collection<Integer> observed, double expected) {
        return observed.stream()
                .mapToDouble(freq -> Math.pow(freq - expected, 2) / expected)
                .sum();
    }

    @Test
    public void testConcurrentModification() throws InterruptedException {
        int threadCount = 10;
        int operationsPerThread = 10000;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 共享的Random实例
        Random sharedRandom = new Random();
        AtomicInteger randomExceptions = new AtomicInteger(0);
        AtomicInteger threadLocalExceptions = new AtomicInteger(0);

        // 启动多个线程同时修改Random的种子
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < operationsPerThread; j++) {
                        try {
                            sharedRandom.setSeed(System.nanoTime());
                            sharedRandom.nextInt();
                        } catch (Exception e) {
                            randomExceptions.incrementAndGet();
                        }

                        try {
                            ThreadLocalRandom.current().nextInt();
                        } catch (Exception e) {
                            threadLocalExceptions.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("Concurrent Modification Test Results:");
        System.out.printf("Random exceptions: %d%n", randomExceptions.get());
        System.out.printf("ThreadLocalRandom exceptions: %d%n", threadLocalExceptions.get());
    }

    @Test
    public void testRandomUUID() {
        int iterations = 100000;
        Statistics randomStats = new Statistics();
        Statistics threadLocalStats = new Statistics();

        for (int i = 0; i < iterations; i++) {
            // 使用Random生成UUID
            long start = System.nanoTime();
            generateUUID(new Random());
            randomStats.addTime(System.nanoTime() - start);

            // 使用ThreadLocalRandom生成UUID
            start = System.nanoTime();
            generateUUID(ThreadLocalRandom.current());
            threadLocalStats.addTime(System.nanoTime() - start);
        }

        System.out.println("UUID Generation Performance:");
        System.out.printf("Random: %s%n", randomStats);
        System.out.printf("ThreadLocalRandom: %s%n", threadLocalStats);
    }

    private UUID generateUUID(Random random) {
        byte[] randomBytes = new byte[16];
        random.nextBytes(randomBytes);
        randomBytes[6] &= 0x0f;  // clear version
        randomBytes[6] |= 0x40;  // set to version 4
        randomBytes[8] &= 0x3f;  // clear variant
        randomBytes[8] |= 0x80;  // set to IETF variant
        return UUID.nameUUIDFromBytes(randomBytes);
    }

    @Test
    public void testLongTermStability() throws InterruptedException {
        Duration testDuration = Duration.ofMinutes(1);
        int checkInterval = 1000000;

        Random random = new Random();
        ThreadLocalRandom threadLocal = ThreadLocalRandom.current();

        Instant startTime = Instant.now();
        long iteration = 0;
        Statistics randomStats = new Statistics();
        Statistics threadLocalStats = new Statistics();

        while (Duration.between(startTime, Instant.now()).compareTo(testDuration) < 0) {
            if (iteration % checkInterval == 0) {
                // 测试Random性能
                long start = System.nanoTime();
                for (int i = 0; i < 1000; i++) {
                    random.nextInt();
                }
                randomStats.addTime(System.nanoTime() - start);

                // 测试ThreadLocalRandom性能
                start = System.nanoTime();
                for (int i = 0; i < 1000; i++) {
                    threadLocal.nextInt();
                }
                threadLocalStats.addTime(System.nanoTime() - start);
            }

            // 持续生成随机数
            random.nextInt();
            threadLocal.nextInt();
            iteration++;
        }

        System.out.println("Long-term Stability Test Results:");
        System.out.printf("Total iterations: %d%n", iteration);
        System.out.printf("Random performance samples: %s%n", randomStats);
        System.out.printf("ThreadLocalRandom performance samples: %s%n", threadLocalStats);
    }

    @Test
    public void testRandomWithCustomThreadPool() throws InterruptedException {
        // 测试不同线程池配置下的性能
        int[] poolSizes = {2, 4, 8, 16};
        int tasksPerPool = 1000000;

        for (int poolSize : poolSizes) {
            // 创建不同类型的线程池
            ExecutorService fixedPool = Executors.newFixedThreadPool(poolSize);
            ExecutorService cachedPool = Executors.newCachedThreadPool();
            ExecutorService workStealingPool = Executors.newWorkStealingPool(poolSize);

            // 测试每种线程池
            System.out.printf("Testing with pool size: %d%n", poolSize);
            testWithThreadPool("Fixed Thread Pool", fixedPool, tasksPerPool);
            testWithThreadPool("Cached Thread Pool", cachedPool, tasksPerPool);
            testWithThreadPool("Work Stealing Pool", workStealingPool, tasksPerPool);

            // 关闭线程池
            fixedPool.shutdown();
            cachedPool.shutdown();
            workStealingPool.shutdown();

            fixedPool.awaitTermination(10, TimeUnit.SECONDS);
            cachedPool.awaitTermination(10, TimeUnit.SECONDS);
            workStealingPool.awaitTermination(10, TimeUnit.SECONDS);

            System.out.println();
        }
    }

    private void testWithThreadPool(String poolType, ExecutorService pool, int taskCount) throws InterruptedException {
        Statistics randomStats = new Statistics();
        Statistics threadLocalStats = new Statistics();
        CountDownLatch latch = new CountDownLatch(taskCount);
        Random sharedRandom = new Random();

        // 提交任务到线程池
        for (int i = 0; i < taskCount; i++) {
            pool.submit(() -> {
                try {
                    // 测试Random
                    long start = System.nanoTime();
                    sharedRandom.nextInt();
                    randomStats.addTime(System.nanoTime() - start);

                    // 测试ThreadLocalRandom
                    start = System.nanoTime();
                    ThreadLocalRandom.current().nextInt();
                    threadLocalStats.addTime(System.nanoTime() - start);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        System.out.printf("%s Results:%n", poolType);
        System.out.printf("Random: %s%n", randomStats);
        System.out.printf("ThreadLocalRandom: %s%n", threadLocalStats);
    }
}
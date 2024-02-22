package jmh;

import com.github.kuangcp.util.StrUtil;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Kuangcp
 * 2024-02-22 09:39
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 1, time = 1)
@Threads(2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class GuideTest {


    //    @Benchmark
    public void uuid() {
        UUID.randomUUID().toString();
    }

    @Benchmark
    public void rand() {
        StrUtil.randomAlpha(32);
    }

    // arrayList 本身是对数组的封装，性能影响不大
    @Benchmark
    public void randArray() {
        StrUtil.randomAlphaA(32);
    }

    // 省去对象创建，效率更好
    @Benchmark
    public void randArrayLocal() {
        StrUtil.randomAlphaAL(32);
    }

    @Test
    public void testGuide() throws Exception {
        Options options = new OptionsBuilder()
                .include(this.getClass().getSimpleName())
                .output("/tmp/jmh-" + this.getClass().getSimpleName() + ".log").build();
        new Runner(options).run();
    }

    @Test
    public void testCompare() throws Exception {
        System.out.println(UUID.randomUUID());
        System.out.println(StrUtil.randomAlpha(32));
    }

    @Test
    public void testDiff() throws Exception {
        Set<String> re = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            re.add(StrUtil.randomAlphaAL(4));
        }
        System.out.println(re.size());

        Set<String> re2 = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            re2.add(StrUtil.randomAlphaA(4));
        }
        System.out.println(re2.size());

    }
}

package jmh;

import com.github.kuangcp.util.StrUtil;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


    @Benchmark
    public void uuid() {
        UUID.randomUUID().toString();
    }

    @Benchmark
    public void rand() {
        StrUtil.randomAlpha(32);
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

}

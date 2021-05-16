package jmh;

import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author kuangcp on 2019-04-21 11:39 PM
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 1, time = 1)
@Threads(3)
@Fork(3)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class StringBuilderBenchmarkTest {

  @Test
  public void test() throws RunnerException {
    String output = "/tmp/StringBuilderBenchmark-" + System.currentTimeMillis() + ".log";
    System.out.println(output);
    Options options = new OptionsBuilder()
        .include(StringBuilderBenchmarkTest.class.getSimpleName())
        .output(output).build();
    new Runner(options).run();
  }

  @Benchmark
  public void testStringAdd() {
    String a = "";
    for (int i = 0; i < 10; i++) {
      a += i;
    }
  }

  @Benchmark
  public void testStringBuilderAdd() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 10; i++) {
      sb.append(i);
    }
    sb.toString();
  }

  @Benchmark
  public void testStringBufferAdd() {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < 10; i++) {
      sb.append(i);
    }
    sb.toString();
  }
}
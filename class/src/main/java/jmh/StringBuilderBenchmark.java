package jmh;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

/**
 * @author kuangcp on 2019-04-21 11:38 PM
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3)
@Measurement(iterations = 5, time = 2)
@Threads(3)
@Fork(3)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class StringBuilderBenchmark {

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
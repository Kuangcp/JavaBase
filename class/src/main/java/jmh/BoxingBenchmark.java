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
 * @author kuangcp
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3)
@Measurement(iterations = 5, time = 2)
@Threads(3)
@Fork(3)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BoxingBenchmark {

  @Benchmark
  public void testBoxing() {
    Integer v = 4;
    for (int i = 0; i < 100; i++) {
      v += 4;
    }
  }

  @Benchmark
  public void testNoBoxing() {
    int v = 4;
    for (int i = 0; i < 100; i++) {
      v += 4;
    }
  }
}

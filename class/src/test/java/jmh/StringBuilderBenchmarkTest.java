package jmh;

import org.junit.Test;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author kuangcp on 2019-04-21 11:39 PM
 */
public class StringBuilderBenchmarkTest {

  @Test
  public void test() throws RunnerException {
    Options options = new OptionsBuilder()
        .include(StringBuilderBenchmark.class.getSimpleName())
        .output("/home/kcp/test/jmh/Benchmark.log").build();
    new Runner(options).run();
  }
}

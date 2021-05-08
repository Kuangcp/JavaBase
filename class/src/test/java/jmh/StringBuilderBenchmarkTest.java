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
    String output = "/tmp/StringBuilderBenchmark-" + System.currentTimeMillis() + ".log";
    System.out.println(output);
    Options options = new OptionsBuilder()
        .include(StringBuilderBenchmark.class.getSimpleName())
        .output(output).build();
    new Runner(options).run();
  }
}

package jmh;

import org.junit.Test;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author kuangcp
 */
public class BoxingBenchmarkTest {

  @Test
  public void testBoxingAndUnBoxing() throws RunnerException {
    String output = "/tmp/StringBuilderBenchmark-" + System.currentTimeMillis() + ".log";
    System.out.println(output);
    Options options = new OptionsBuilder()
        .include(BoxingBenchmark.class.getSimpleName())
        .output(output).build();
    new Runner(options).run();
  }

}

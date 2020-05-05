package com.github.kuangcp.serialize.json;

import com.github.kuangcp.serialize.Person;
import com.github.kuangcp.serialize.json.speed.FastJsonTool;
import com.github.kuangcp.serialize.json.speed.GsonTool;
import com.github.kuangcp.serialize.json.speed.JacksonTool;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 1, time = 1)
@Threads(2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ReadJsonTest {

  private static final String json = "{\"name\":\"one\",\"address\":\"any province\",\"phone\":null}";

  @Benchmark
  public void testGsonRead() throws IOException {
    new GsonTool().fromJSON(json, Person.class);
  }

  @Benchmark
  public void testJacksonRead() throws IOException {
    new JacksonTool().fromJSON(json, Person.class);
  }

  @Benchmark
  public void testFastJsonRead() throws IOException {
    new FastJsonTool().fromJSON(json, Person.class);
  }

  @Test
  public void testCompareRead() throws Exception {
    Options options = new OptionsBuilder()
        .include(ReadJsonTest.class.getSimpleName())
        .output("/tmp/" + ReadJsonTest.class.getSimpleName() + ".log").build();
    new Runner(options).run();
  }
}
package com.github.kuangcp.serialize.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.kuangcp.serialize.Person;
import com.github.kuangcp.serialize.json.speed.FastJsonTool;
import com.github.kuangcp.serialize.json.speed.GsonTool;
import com.github.kuangcp.serialize.json.speed.JacksonTool;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
 * @author https://github.com/kuangcp on 2020-05-06 01:12
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 1)
@Measurement(iterations = 10, time = 1)
@Threads(2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class WriteJsonTest {

  private static final int DATA_SIZE = 30;
  private static final List<Person> personList = IntStream.range(1, DATA_SIZE)
      .mapToObj(i -> new Person("name" + i)).collect(Collectors.toList());

  @Benchmark
  public void jacksonWrite() throws JsonProcessingException {
    new JacksonTool().toJSON(personList);
  }

  @Benchmark
  public void fastJsonWrite() throws JsonProcessingException {
    new FastJsonTool().toJSON(personList);
  }

  @Benchmark
  public void gsonWrite() throws JsonProcessingException {
    new GsonTool().toJSON(personList);
  }

  @Test
  public void testCompareRead() throws Exception {
    Options options = new OptionsBuilder()
        .include(WriteJsonTest.class.getSimpleName())
        .output("/tmp/" + WriteJsonTest.class.getSimpleName() + ".log").build();
    new Runner(options).run();
  }
}

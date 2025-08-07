package com.github.kuangcp.serialize.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.kuangcp.serialize.Person;
import com.github.kuangcp.serialize.json.speed.FastJsonTool;
import com.github.kuangcp.serialize.json.speed.GsonTool;
import com.github.kuangcp.serialize.json.speed.JacksonTool;
import org.junit.Assert;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2020-05-06 01:12
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

    @Test
    public void testSerializeDate() throws Exception {
        DateObj obj = new DateObj();
        obj.day = LocalDate.of(2000, 1, 2);
        obj.dayZone = Date.from(LocalDateTime.of(obj.getDay(), LocalTime.MIN).atZone(ZoneOffset.UTC).toInstant());
        obj.dayZone2 = Date.from(LocalDateTime.of(obj.getDay(), LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant());
        {
            String a = JacksonTool.mapper.writeValueAsString(obj);
            System.out.println(a);

            DateObj read = JacksonTool.mapper.readValue(a, DateObj.class);
            System.out.println(read);
            Assert.assertEquals(read.getDayZone(), obj.dayZone);
            // 因为Jackson在序列化和反序列化Date对象到ymd字符串 而不是对象时，采用的是UTC，
            // 如果从无时区转为Date对象过程中使用的系统+8时区转换就会有时差问题，从天数来看就会看起来少了一天
            // jackson 处理 java.time下的类需要引入 jsr310 依赖
            Assert.assertNotEquals(read.getDayZone2(), obj.dayZone2);
        }
        {
            String json = GsonTool.gson.toJson(obj);
            System.out.println(json);
            DateObj read = GsonTool.gson.fromJson(json, DateObj.class);
            System.out.println(read);
            // gson 序列化为了对象，时区未丢 所以正常。 即使调整为ymd格式，和fastjson一样是用系统时区去反序列化，所以不会有问题
            // 但是gson有另一个问题，未指定格式时序列化的字符串是随环境变化（会出现不同机器格式不一致问题），jackson默认是 ISO-8601
            Assert.assertEquals(read.getDayZone(), obj.dayZone);
            Assert.assertEquals(read.getDayZone2(), obj.dayZone2);
        }

    }
}

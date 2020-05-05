package com.github.kuangcp.serialize.json.speed;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

/**
 * @author https://github.com/kuangcp on 2020-05-06 00:33
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3)
@Measurement(iterations = 10, time = 1)
@Threads(4)
@Fork(2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SpeedBenchMark {

}

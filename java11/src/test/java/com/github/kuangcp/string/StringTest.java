package com.github.kuangcp.string;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author https://github.com/Kuangcp on 2019-06-23 15:38
 */
@Slf4j
public class StringTest {

  @Test
  public void testSimple() {
    assertTrue("     ".isBlank());

    assertThat(" Java  ".strip(), equalTo("Java"));

    assertThat(" Java ".stripTrailing(), equalTo(" Java"));
    assertThat(" Java ".stripLeading(), equalTo("Java "));

    assertThat("Java".repeat(3), equalTo("JavaJavaJava"));

    assertThat("A\nB\nC".lines().count(), equalTo(3L));
  }

  @Test
  public void testStream() {
    String result = "1\n222\n\t23\t".lines()
        .filter(Objects::nonNull)
        .filter(v -> v.length() == 1)
        .collect(Collectors.joining(","));

    log.info("result={}", result);
  }
}

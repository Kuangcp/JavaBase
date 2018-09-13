package com.github.kuangcp.serialize.json;

import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 18-9-13-下午2:16
 */
@Slf4j
public class GsonTest {

  @Test
  public void testRead() {
    Gson gson = new Gson();

    Code code = gson.fromJson("{\"code\":12, \"name\":\"ui\"}", Code.class);
    log.info(": code={}", code);
  }

  @Data
  class Code {

    private int code;

  }
}

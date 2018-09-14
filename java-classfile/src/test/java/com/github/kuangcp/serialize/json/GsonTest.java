package com.github.kuangcp.serialize.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author kuangcp on 18-9-13-下午2:16
 */
@Slf4j
public class GsonTest {

  private Gson gson = new Gson();

  @Test
  public void testRead() {

    Code code = gson.fromJson("{\"code\":12, \"name\":\"ui\"}", Code.class);
    log.info(": code={}", code);
  }


  /**
   * read a standard json string
   */
  @Test
  public void testRead2() {
    String origin = "{\"code\":3131,\"playerId\":216,\"title\":\"new email\","
        + "\"content\":\"send by admin platform\","
        + "\"attachment\":\"[{\\\"func\\\":\\\"addItem\\\", \\\"args\\\":[\\\"item_1\\\", 100]}]\"}";

    JsonElement element = new JsonParser().parse(origin);
    JsonObject rawJsonObject = element.getAsJsonObject();
    int code = rawJsonObject.get("code").getAsInt();
    log.info("result: code={}", code);
    log.info(": jsonElement={}", element);
  }

  @Data
  class Code {

    private int code;

  }
}

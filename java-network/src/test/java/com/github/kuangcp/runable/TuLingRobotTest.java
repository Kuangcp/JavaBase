package com.github.kuangcp.runable;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;

/**
 * created by https://gitee.com/gin9
 *
 * 调用图灵机器人平台接口 适合使用js来做
 * http://www.java2s.com/Code/Jar/h/Downloadhttpclient423jar.htm
 *
 * @author kuangcp on 3/10/19-11:49 PM
 */
@Slf4j
public class TuLingRobotTest {

  private OkHttpClient client = new OkHttpClient();

  private Optional<String> get(String url) throws IOException {
    Request request = new Request.Builder()
        .url(url).build();

    try (Response response = client.newCall(request).execute()) {
      if (response.body() != null) {
        return Optional.ofNullable(response.body().string());
      }
      return Optional.empty();
    }
  }

  @Test
  public void testGet() throws IOException {
    String baseURL = "http://www.tuling123.com/openapi/api?key=c8d9f9fd7a4f46609686020354745f25&info=";
    String input = "翻译 appropriate 详情";
    String INFO = URLEncoder.encode(input, "utf-8");

    Optional<String> result = get(baseURL + INFO);
    assert result.isPresent();

    System.out.println(result.get());
  }
}

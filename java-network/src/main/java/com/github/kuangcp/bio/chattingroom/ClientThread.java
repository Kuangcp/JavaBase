package com.github.kuangcp.bio.chattingroom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Myth on 2017/4/2 0002
 */
@Slf4j
public class ClientThread implements Runnable {

  private String name;
  private BufferedReader br;

  ClientThread(String name, Socket s) throws Exception {
    this.name = name;
    br = new BufferedReader(new InputStreamReader(s.getInputStream()));
  }

  @Override
  public void run() {
    try {
      String content;
      //不断读取输入流中内容并打印输出
      while ((content = br.readLine()) != null) {
        if (content.startsWith(name)) {
          log.info("receive: self={}", content);
          continue;
        }

        log.info("receive: content={}", content);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
}

package com.github.kuangcp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.github.kuangcp.**.dao")
@SpringBootApplication()
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}

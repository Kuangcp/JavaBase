package com.github.kuangcp.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.github.kuangcp.*.dao")
public class MybatisPlusConfig {

}

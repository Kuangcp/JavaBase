package com.github.kuangcp.proxy.salary.jdkproxy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Security implements Interceptor{

	@Override
	public void interceptor() {
		log.info("security control");
	}
}

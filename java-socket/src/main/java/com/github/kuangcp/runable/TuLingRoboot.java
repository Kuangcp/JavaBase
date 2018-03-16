package com.github.kuangcp.runable;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;

public class TuLingRoboot {
	/**
	 * 调用图灵机器人平台接口 适合使用js来做
	 * http://www.java2s.com/Code/Jar/h/Downloadhttpclient423jar.htm
	 * 需要导入的包：commons-logging-1.0.4.jar、 httpclient-4.3.1.jar、httpcore-4.3.jar
	 *
	  compile group: 'org.apache.httpcomponents', name: 'httpclient', version: '4.5.2'
	 compile group: 'org.apache.httpcomponents', name: 'httpcore', version: '4.4.4'
	 */
	private static Logger logger = LoggerFactory.getLogger(TuLingRoboot.class);
	public static void main(String[] args) throws IOException {

		String input = "你叫什么";
		String INFO = URLEncoder.encode(input, "utf-8");
		String requesturl = "http://www.tuling123.com/openapi/api?key=c8d9f9fd7a4f46609686020354745f25&info=" + INFO;
		HttpGet request = new HttpGet(requesturl);
		HttpResponse response = HttpClients.createDefault().execute(request);

		// 200即正确的返回码
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity());
			logger.info("返回结果：\n" + result);
		}
	}
}

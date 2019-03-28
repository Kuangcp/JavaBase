package com.github.kuangcp.aop.exception;
/**
 * 切面
 * @author Administrator
 *
 */
public class MyException {
	/**
	 * 写一个通知为异常通知
	 */
	public void getExcpetionMessage(Throwable ex){
		System.out.println(ex.getMessage());
	}
}

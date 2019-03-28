package com.github.kuangcp.exception;
/**
 * 只是一个数据格式而已，作为各个类通信的数据格式，指明某类某方法要执行
 * @TODO 
 * @author  Myth
 * @date 2016年11月21日 上午9:49:37
 */
public class ServiceMapping {
	private String serviceClass;//封装service的全名
	private String method;//某一个service的方法
	
	public String getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	
}	

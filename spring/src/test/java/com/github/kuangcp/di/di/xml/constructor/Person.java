package com.github.kuangcp.di.di.xml.constructor;

public class Person {
	private Long pid;
	public Long getPid() {
		return pid;
	}

	public String getPname() {
		return pname;
	}

	public Student getStudent() {
		return student;
	}

	private String pname;
	private Student student;
	
	public Person(Long pid,String pname){
		this.pid = pid;
		this.pname = pname;
	}
	
	public Person(String pname,Student student){
		this.pname = pname;
		this.student = student;
	}
}

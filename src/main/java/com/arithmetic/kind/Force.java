package com.arithmetic.kind;

import java.util.List;



/**
 * 暴力求解算法
 * @author  Myth
 * @date 2016年9月6日 上午8:25:16
 * @TODO 多个函数（具体问题）体现了暴力求解方法
 */
public class Force {
	public static void main(String []s){
		
	}
	public void createKB(){
		//输入课程
		Mysql db = new Mysql("test","root","ad");
		List<String []>course = db.SelectReturnList("select * from course");
		
		//暴力随机求解
	}
}

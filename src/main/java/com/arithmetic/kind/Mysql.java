package com.arithmetic.kind;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



/**
 * 切记要导JAR驱动包，还有配置好URL
 * @author Myth
 * @date 2016年7月24日
 */
public class Mysql {
	/**
	 * 封装的函数：
	 * 		查询返回rs
	 * 		查询返回List
	 * 		增删改 返回boolean
	 *		事务性批量插入数据 返回boolean
	 */
	private static int count = 0;
	private PreparedStatement ps = null;
	private Connection cn = null;
	private ResultSet rs = null;
	private String Driver;
	private String URL;

//	public static void main(String [] a){
//		Mysql db = new Mysql("runable","root","ad");
//		String sql = "insert into inserts values (2,'q');";//insert into inserts values (3,'w');";
//		boolean flag = db.updSQL(sql);
//		System.out.println("是否成功："+flag);
//		
//	}
	/**
	 * 连接本地数据库的方法
	 * @param db 数据库
	 * @param user 用户
	 * @param pass 密码
	 */
	public Mysql(String db,String user,String pass){
		Driver = "com.mysql.jdbc.Driver";
		URL="jdbc:mysql://localhost:3306/"+db+"?user="+user+"&password="+pass+"&userUnicode=true&characterEncoding=UTF8";
	}
	/**
	 * 默认是从文件中读取 
	 */
	public Mysql(){
		Config con = new Config("mysql.properties");
		this.Driver = con.getString("Driver");
		this.URL = con.getString("URL");
	}
	/**获取数据库连接*/
	public Connection getConnection(){
		try {
			Class.forName(Driver);
			cn = DriverManager.getConnection(URL);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取连接，异常！");
		}
		return cn;
	}
	
/**查询全部的操作 返回值是ResultSet 切记使用完后要finally关闭*/
	public ResultSet SelectAll(String sql){
		count++;
		try {
			Class.forName(Driver);
			cn = DriverManager.getConnection(URL);
			ps=cn.prepareStatement(sql);
			
			rs=ps.executeQuery();
		} catch (Exception e) {
		}finally {
			//不能关闭
//			this.closeAll();
		}
		System.out.println("做了"+count+"次查询操作");
		return rs;
	}
	/**
	 * SQL查询并返回List集合
	 * @param sql
	 * @return List<String[]> 一行是一个String[] 按查询的字段顺序
	 */
	public List<String []> SelectReturnList(String sql){
		int Cols;
		List <String []> data = new ArrayList<String [] >();
		ResultSet rs = SelectAll(sql);
		try {
			Cols = rs.getMetaData().getColumnCount();//获取总列数
			while(rs.next()){
				//为什么放在while外面就会出现最后一组元素覆盖整个数组？
				String [] row = new String [Cols];
				for (int i=0;i<Cols;i++){
					row[i] = rs.getString(i+1);
				}
				data.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			this.closeAll();
		}
		return data;
	}
/**把增删改 合在一起 返回值是 布尔值 
 * 各种连接已经关闭了不用再次关闭了
 * SQL只能输一句，不能多句运行
 */
	public boolean updSQL(String sql){
		boolean flag = true;
		try{
			Class.forName(Driver);
			cn = DriverManager.getConnection(URL);
			ps=cn.prepareStatement(sql);
			int i=ps.executeUpdate();
			System.out.print("    增删改查成功_"+i+"_行受影响-->");
			if(i!=1){
				flag=false;
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("增删改查失败");
			flag=false;
		}finally {
			this.closeAll();
		}
		return flag;
	}
	/**参数是SQL语句数组，插入多条数据并采用了事务*/
	public boolean BatchInsertOfAffair(String [] sqls){
		boolean success = true;
		try{
			Class.forName(Driver);
			cn = DriverManager.getConnection(URL);
			cn.setAutoCommit(false);//取消自动提交
			int i=0;
			for (String sql:sqls){
				i++;
				ps=cn.prepareStatement(sql);
				ps.executeUpdate();
				System.out.println("第"+i+"条记录插入成功");
			}
			cn.commit();//无异常再提交
		}catch(Exception e){
			success = false;
			try {
				cn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			System.out.println("增删改查失败");
		}finally {
			try {
				cn.setAutoCommit(true);//改回来
			} catch (SQLException e) {
				e.printStackTrace();
			}
			this.closeAll();
		}
		return success;
	}
	/**关闭数据库资源*/
	public void closeAll(){
		//关闭资源 后打开先关闭
		try {
			if(rs!=null) rs.close();
			if(ps!=null) ps.close();
			if(cn!=null) cn.close();
		} catch (SQLException e) { 
			e.printStackTrace();
		}
		System.out.println("正常-关闭资源");
	}
}

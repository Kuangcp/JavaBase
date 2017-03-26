package com.io.inout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * 文本文件的拷贝： 字节流或者字符流   其他文件只能字节流
 *
 *下面介绍3种流处理方式:
 *
 *
 *    注意：流没有关闭的话，记事本打开也没有东西
 */
public class Stream {

/**字符流*/
	//操作 char
	/*public static void main(String [] j){

		FileReader fr = null; //输入流
		FileWriter fw = null; //输出流

		try {
			//创建输入对象
			fr = new FileReader("E:File.txt");
			//创建输出对象
			fw = new FileWriter("D:File.txt");
			//读入内存
			char p[] = new char[512];
			int n=0;
			while((n=fr.read(p))!=-1){
//				fw.write(p);//不足512字符的话后面会乱码
				fw.write(p,0,n);//指定0-n长度
//				String f  = new String(p, 0, n);
//				System.out.println(f);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}finally{
			try {
				fr.close();
				fw.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}*/



/**字节流*/
	//操作字节byte
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub

		FileInputStream fis = null;
		FileOutputStream fos = null;

		try {
			fis = new FileInputStream("E:File.txt");
			fos = new FileOutputStream("D:File.txt");

			byte [] buffer = new byte[1024];
			int n = 0;
			while((n=fis.read(buffer))!=-1){
				//已经进入了输入流1024字节
				//输出到指定文件
				fos.write(buffer);
			}


		} catch(Exception e){
			e.printStackTrace();
		}
		finally {
			// TODO: handle finally clause
			try {
				fis.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}*/
	/**缓冲字符流*/
	//读取一行 \r 或者 \n 结束  \r:回车（return）\n；换行（nextLine）
	//BufferReader 和 BufferWriter 直接操作String

	public static void main(String [] e){


		BufferedReader br = null;
		BufferedWriter bw = null;


		try {
			//先进入Reader 再封装成BufferReader
//			FileReader fr = new FileReader("E:File.txt");
//			br = new BufferedReader(fr);
			br = new BufferedReader(new FileReader("E:File.txt"));
			//创建FileWriter对象
			bw = new BufferedWriter(new FileWriter("D:Fil.txt"));
			//循环读取文件
			String s = "";
			while((s=br.readLine())!=null){
				System.out.println(s.substring(0, 1));
				//输出
				bw.write(s+"\r\n");
			}
			System.out.println("\n成功读取并写入");

		} catch (Exception e2) {
			// TODO: handle exception
			e2.printStackTrace();
		}finally{
			try {
				br.close();
				bw.close();
			} catch (Exception e3) {
				// TODO: handle exception
				e3.printStackTrace();
			}
		}
	}
}

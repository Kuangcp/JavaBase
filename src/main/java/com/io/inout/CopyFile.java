package com.io.inout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * 关于相对路径文件的复制问题
 * 读取文件  输出
 *
 * InputStream   所有输入流基类（建立流）（字节流）
 * InputStreamReader 将字节流  转换成 字符流
 * BufferedReader  从字符流输入流读取文件
 *
 */
public class CopyFile {

	public static void main(String[] args) {
		InputStream input = null;
		BufferedReader br = null;
		InputStreamReader ids =null;
		BufferedWriter bw = null;
		OutputStream out = null;
		OutputStreamWriter os = null;


		try{
			input = new FileInputStream("./src/files/files.txt");
			ids = new InputStreamReader(input);
			br = new BufferedReader(ids);


			out = new FileOutputStream("./src/files/filess.txt");
			os = new OutputStreamWriter(out);
			bw = new BufferedWriter(os);



			bw.flush();//一定要清除缓存.

			String L="";
			while((L=br.readLine())!=null){
				bw.write(L+"\r\n");
			}
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("读取数据有异常");
		}finally {
			try {
				//先打开，后关闭
				input.close();
				ids.close();
				br.close();
				System.out.println("全部关闭了输入流");

			} catch (Exception e2) {
				e2.printStackTrace();
				System.out.println("有异常");
			}

			try {
				//先打开，后关闭
				bw.close();
				os.close();
				out.close();
				System.out.println("保存数据并且关闭全部输出流");
			}catch (Exception e2) {
				e2.printStackTrace();
				System.out.println("有异常");
			}

		}
	}
}

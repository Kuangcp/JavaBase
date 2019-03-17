package com.github.kuangcp.inout;

import com.github.kuangcp.io.ResourceTool;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;

/**
 * 关于相对路径文件的复制问题
 *
 * InputStream   所有输入流基类（建立流）（字节流）
 * InputStreamReader 将字节流  转换成 字符流
 * BufferedReader  从字符流输入流读取文件
 *
 * 注意: 如果输出流尚未关闭 内存的数据尚未刷新到硬盘上, 会导致不一致的情况
 */
@Slf4j
class CopyFile {

  // 字节流 字节流转换为字符流 缓冲字符流 ->
  void copyFileWithSixChannel(String from, String dest) {
    InputStream inputStream = null;
    InputStreamReader inputStreamReader = null;
    BufferedReader bufferedReader = null;

    OutputStream outputStream = null;
    OutputStreamWriter outputStreamWriter = null;
    BufferedWriter bufferedWriter = null;

    try {
      // 在 idea 中就是 project(工作目录) 根目录+path
      inputStream = new FileInputStream(from);
      inputStreamReader = new InputStreamReader(inputStream);
      bufferedReader = new BufferedReader(inputStreamReader);

      outputStream = new FileOutputStream(dest);
      outputStreamWriter = new OutputStreamWriter(outputStream);
      bufferedWriter = new BufferedWriter(outputStreamWriter);

      //刷新缓存到硬盘中
      bufferedWriter.flush();

      String L;
      while ((L = bufferedReader.readLine()) != null) {
        bufferedWriter.write(L + "\r\n");
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        // 先打开后关闭
        ResourceTool.close(bufferedReader, inputStreamReader, inputStream);
        log.info("close all input stream");

        ResourceTool.close(bufferedWriter, outputStreamWriter, outputStream);
        log.info("close all output stream");
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  // 字节流
  void copyFileByByte(String from, String dest) {
    FileInputStream inputStream = null;
    FileOutputStream outputStream = null;

    try {
      inputStream = new FileInputStream(from);
      outputStream = new FileOutputStream(dest);

      // 字节缓冲 数组
      byte[] buffer = new byte[1024];
      while (inputStream.read(buffer) != -1) {
        outputStream.write(buffer);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        ResourceTool.close(inputStream, outputStream);
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  // 字符流
  void copyFileByChar(String from, String dest) {
    FileReader fileReader = null; //输入流
    FileWriter fileWriter = null; //输出流

    try {
      fileReader = new FileReader(from);
      fileWriter = new FileWriter(dest);

      //读入内存
      char[] p = new char[512];
      int n;
      while ((n = fileReader.read(p)) != -1) {
//				fileWriter.write(p);//不足512字符的话后面会乱码
        fileWriter.write(p, 0, n);//指定0-n长度

        String cacheContent = new String(p, 0, n);
        System.out.println(cacheContent);
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        ResourceTool.close(fileReader, fileWriter);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  // 缓冲字符流 按行读取
  void copyFileByCharBuffer(String from, String dest) {
    BufferedReader reader = null;
    BufferedWriter writer = null;

    try {
      reader = new BufferedReader(new FileReader(from));
      writer = new BufferedWriter(new FileWriter(dest));

      String s;
      while ((s = reader.readLine()) != null) {
        log.info("line: {}", s);
        //输出
        writer.write(s + "\r\n");
      }
      log.info("成功读取并写入");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        ResourceTool.close(reader, writer);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  // Java7 中引入的 Files
  void copyByFiles(Path from, Path dest) throws IOException {
    Files.copy(from, dest);
  }
}

package com.github.kuangcp.inout;

import com.github.kuangcp.io.ResourceTool;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import lombok.extern.slf4j.Slf4j;

/**
 * 关于相对路径文件的复制问题
 * 读取文件  输出
 *
 * InputStream   所有输入流基类（建立流）（字节流）
 * InputStreamReader 将字节流  转换成 字符流
 * BufferedReader  从字符流输入流读取文件
 */
@Slf4j
class CopyFile {

  // 字节流 字符流 读入 ->
  void copyFileWithSixChannel(String from, String dest) {
    InputStream inputStream = null;
    BufferedReader bufferedReader = null;
    InputStreamReader streamReader = null;

    BufferedWriter bufferedWriter;
    OutputStream outputStream = null;
    OutputStreamWriter outputStreamWriter = null;

    try {
      // 在 idea 中就是 project(工作目录) 根目录+path
      inputStream = new FileInputStream(from);
      streamReader = new InputStreamReader(inputStream);
      bufferedReader = new BufferedReader(streamReader);

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
        ResourceTool.close(bufferedReader, streamReader, inputStream);
        log.info("close all input stream");

        ResourceTool.close(bufferedReader, outputStreamWriter, outputStream);
        log.info("close all output stream");
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  // TODO 所有方式
}

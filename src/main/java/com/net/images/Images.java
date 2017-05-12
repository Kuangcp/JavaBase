package com.download;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by mythos on 17-5-5.
 * 多线程下载图片
 */
public class Images implements Runnable{
    List<String[]> images;
//    public static void main(String[] args) throws Exception {
//        download("http://mm.howkuai.com/wp-content/uploads/2014a/11/54/01.jpg", "/home/mythos/Pictures/meizi/laozizhu.com.gif");
//    }

    /**
     * 下载文件到本地
     *
     * @param urlString
     *          被下载的文件地址
     * @param filename
     *          本地文件名
     * @throws Exception
     *           各种异常
     */
    public void download(String urlString, String filename) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        // 输入流
        InputStream is = con.getInputStream();
        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        OutputStream os = new FileOutputStream(filename);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }

    public void run() {
        while(true) {
            try {
                for (String[] temp : images) {
                    download(temp[0], temp[1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

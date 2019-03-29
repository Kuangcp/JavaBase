package com.github.kuangcp.file;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 搞定，成功在于找到特点，然后拆分，保存
 * Created by Myth on 2017/1/10 0010 - 22:25
 */
public class CutTxt {

    //拆分文件根目录
    static String PATH = "E:\\GCDD\\";
    //源文件目录
    static String realFile="E:\\GCD.txt";
    static List<String> rows = new ArrayList<String>();
    static List<String> titles = new ArrayList<String>();
    public static void main(String[] a) {
        ReadFile();
        for(int i=0;i<rows.size();i++){
            String row = rows.get(i);
            String title = titles.get(i);
            System.out.println("- ["+title+"](https://github.com/Kuangcp/GhostPushLight/blob/master/data/"+row+".txt)");
        }

    }

    //读取指定路径下的文件
    public static void ReadFile() {
        int count = 0;

        InputStream input = null;
        InputStreamReader ids = null;
        BufferedReader br = null;

        try {
//            input = FileReader.class.getResourceAsStream("F:\\WorkSpace\\myth\\IO\\File\\a.java");
//            input = new FileInputStream(new File("F:\\WorkSpace\\myth\\IO\\File\\a.java"));
            input = new FileInputStream(new File(realFile));
            System.out.println("文件字节(byte)大小："+input.available()); //尝试能否找到打开 并且是获取字节数

            //以GBK读取文件
            ids = new InputStreamReader(input, Charset.forName("UTF-8"));
            br = new BufferedReader(ids);

            List<String> chapters = new ArrayList<String>();
            String content = null;

            //读取直到文件最末尾
            while ((content = br.readLine()) != null) {
                count++;
                //截取失败就返回原字符串 所以长度是1
//                String [] s = content.split("javascript");
//                System.out.println(count+" : "+s.length);
                String test[] = content.split("javascript");
                if(test.length==1){
                    chapters.add(content);
//                    System.out.println(count);
                }else{
                    //截取到了章节位置
                    if(count>2)
                        //SaveFile(chapters);
                    chapters.clear();
//                    System.out.println(count+"截取成功");
                    chapters.add(count+"");
                    rows.add(count+"");
                    titles.add("【"+test[0]+"    】    "+test[1]);
                    //chapters.add(count+" "+runable[0]+" "+runable[1]);
                }

//                System.out.println(count + "." + content);
//                new String(content.getBytes("GBK"),"UTF-8")
            }
//            System.out.println(count+"循环退出，到达文件尾");
            //SaveFile(chapters);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {//从里到外的封装关系 来关闭流 一旦顺序错了就有莫名奇妙的错误
            try {
                input.close();
                ids.close();
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //将数组转存成文件
    public static void  SaveFile(List<String> datas){
        BufferedWriter bw = null;
        OutputStream out = null;
        OutputStreamWriter os = null;
//        for(String e:dataStack){
//            System.out.println(e);
//        }
        if(datas.size()>1) {
            try {
                String filename = PATH + datas.get(0) + ".txt";
                filename.split("");
                System.out.println("文件名："+filename);
                if(filename.length()<40) {
                    out = new FileOutputStream(filename.trim());
                }
                os = new OutputStreamWriter(out);
                bw = new BufferedWriter(os);

                for (int i = 0; i < datas.size(); i++) {
                    bw.write(datas.get(i)+"\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("IO有异常");
            } finally {
                try {
                    if (bw != null) bw.close();
                    if (os != null) os.close();
                    if (out != null) out.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                    System.out.println("资源关闭异常");
                }
            }
        }
    }
}

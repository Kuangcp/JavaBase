package com.net.images;

import com.myth.mysql.Mysql;

import java.util.List;

/**
 * Created by mythos on 17-5-5.
 */
public class download {
    private static int threadNum = 10;
    private static int counts;

    public static void main(String []s) throws Exception{

//        download d = new download();
//        counts = d.getNums();
//        for(int i=0;i<threadNum;i++){
//            d.downloads(i*counts/threadNum,counts/threadNum);
//        }
    }
    public void downloads(int start,int num)throws Exception{
        System.out.println("启动一个线程");
        Images i = new Images();
        i.images = getData(start,num);
        Thread t = new Thread(i);
        t.start();

    }
    public List<String[]> getData(int start,int num) throws Exception{
        Mysql db = new Mysql("jadmin","3306","root","mysql1104");

        List<String[]> list=  db.queryReturnList("select URL,Path from t_mz_pic limit "+start+","+num);
        return list;
//        int count = 0;
//        for(String []a:list){
////            download(a[0],"/home/mythos/Pictures/meizi/"+count+".jpg");
//            System.out.println(a[0]);
//        }
    }
    //返回总数
    public int getNums() throws Exception{
        Mysql db = new Mysql("jadmin","3306","root","mysql1104");
        List<String[]> list=  db.queryReturnList("select count(*) from t_mz_pic");
        return Integer.parseInt(list.get(0)[0]);
    }

}

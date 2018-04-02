package com.github.kuangcp.valatile;

import java.util.concurrent.TimeUnit;

/**
 * Created by https://github.com/kuangcp
 * 因为怀疑是Groovy编译问题, 特意写了Java版, 还是正常的, 需要找另一个重排序Demo了
 * @author kuangcp
 * @date 18-4-2  上午9:28
 */
public class ResortJavaDemo {
    private static boolean stop;
    public static void main(String[]s) throws InterruptedException {
        Thread workThread = new Thread(() -> {
            int i = 0;
            while(!stop){
                i++;
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("i"+i);
            }
        });
        workThread.start();
        TimeUnit.SECONDS.sleep(3);
        stop = true;
    }
}

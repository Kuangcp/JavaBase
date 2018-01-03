package com.threads;

/**
 * Created by https://github.com/kuangcp on 17-8-20  下午8:46
 * TODO 线程的生命周期，状态转化
 */
public class ThreadStatusTransfer {
    class ThisThread extends Thread{
        private boolean runFlag;

        @Override
        public void run() {
            while (runFlag){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Running");
                try {
//                    this.wait();
                    Thread.sleep(90);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public boolean isRunFlag() {
            return runFlag;
        }

        public void setRunFlag(boolean runFlag) {
            this.runFlag = runFlag;
        }
    }

    public static void main(String[] a){
        ThreadStatusTransfer transfer = new ThreadStatusTransfer();
        transfer.test();
    }
    public void test(){
        ThisThread thisThread = new ThreadStatusTransfer().new ThisThread();
        thisThread.setRunFlag(true);
        thisThread.start();
        try {
            Thread.sleep(2000);
            this.notify();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

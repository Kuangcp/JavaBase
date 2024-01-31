package com.github.kuangcp.volatiles;

/**
 * https://www.cnblogs.com/jian0110/p/9369096.html
 * @author kuangcp
 * 2024-01-31 17:31
 */
public class ThisEscape {

    final int i;
    int j;

    public ThisEscape() {
        i = 1;
        j = 1;
        new Thread(new RunnableTest()).start();
    }

    // 内部类实现Runnable：引用外部类
    private class RunnableTest implements Runnable {
        @Override
        public void run() {
            try {
                System.out.print(ThisEscape.this.j);
            } catch (NullPointerException e) {
                System.out.println("发生空指针错误：普通变量j未被初始化");
            }
            try {
                System.out.print(ThisEscape.this.i);
            } catch (NullPointerException e) {
                System.out.println("发生空指针错误：final变量i未被初始化");
            }
            ThisEscape.this.j += 1;
        }
    }
}
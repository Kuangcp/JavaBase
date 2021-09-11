/**
 * 如果有多个线程因等待同一个对象的标志而处于阻塞状态时，当该对象的标志位（对象锁/文件锁）恢复到1状态时
 * 只会有一个线程能够进入同步代码执行，其他的线程仍然处于阻塞的状态
 *     俩线程 并发 我读你 你读我 互斥
 *     就会容易发生死锁 要尽量避免
 * 
 */
package thread.tryone;

public class Thread4 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//定义三个售票窗口  其实不是三个对象，这样的话初始化这里都是2000张，每个窗口都当成一个线程来理解就好了
		
		TicketWindow tw1 = new TicketWindow();
//		TicketWindow tw2 = new TicketWindow();
//		TicketWindow tw3 = new TicketWindow();
		
		//把一个对象封装入三个线程里 和三个对象分别装的区别？
		   //就是说，三个线程都在各自处理各自的对象，并没有并发，同步的问题
		   //可是一旦有静态的（共享性的成员）就会有并发的那些问题，而且出现的问题还都是随机的
		Thread t1 = new Thread(tw1);
		Thread t2 = new Thread(tw1);
		Thread t3 = new Thread(tw1);
		
		t1.start();
		t2.start();
		t3.start();
	}

}

//售票窗口
class TicketWindow implements Runnable {
	//一共两千张票
	private static int nums = 200;
	@SuppressWarnings("unused")
	private Dog3 dog = new Dog3();
	
	
	public void run (){
		
		while (true){
			
			//保证其原子性  (同步代码块)
			/**   不用同步的锁
			 *   如果睡眠时间够长，使用三个对象分别封装 就不会容易出现并发的问题
			 *   但是时间一旦短了呢 就会有明显的错误*/
			synchronized (this) {
//	  synchronized (dog) 括号里可以放任意的对象只是利用对象的一个对象锁功能，
//	         相当于用这个对象做一个标志性的状态信息， 0 1 （标志位）
				//先判断是否还有票
				if (nums >0){
					//显示售票信息、
					
					System.out.println(""+ Thread.currentThread().getName()+"正在售出第 "+nums+"张票");
					try {
						Thread.sleep(100);	//出票速度 一秒一张
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					nums--;
				}else {
					//售票结束
					break;
				}
			}
		}
	}
}

class Dog3{
	
}

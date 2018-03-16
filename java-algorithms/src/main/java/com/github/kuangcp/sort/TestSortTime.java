package com.github.kuangcp.sort;

import java.util.ArrayList;
import java.util.List;

/**
 * 快速排序法有时候能跑，有时候就报错说下标越界，是因为二分法没有检查游标上移的情况
 * 测试不同的排序方法，比较其各自所费时间
 *
 * 实验：
 * 		数据量是:10000  数据范围是:0~1000
		##quick     :||耗时：<31ms>   格式：<0h:0m:0s:31ms>
		##bubble     :||耗时：<641ms>   格式：<0h:0m:0s:641ms>
		##insert     :||耗时：<145ms>   格式：<0h:0m:0s:145ms>
		##select     :||耗时：<234ms>   格式：<0h:0m:0s:234ms>
		##Shell      :||耗时：<453ms>   格式：<0h:0m:0s:453ms>

		数据量是:10000  数据范围是:0~60
		##quick     :||耗时：<32ms>   格式：<0h:0m:0s:32ms>
		##bubble     :||耗时：<547ms>   格式：<0h:0m:0s:547ms>
		##insert     :||耗时：<125ms>   格式：<0h:0m:0s:125ms>
		##select     :||耗时：<88ms>   格式：<0h:0m:0s:88ms>
		##Shell      :||耗时：<156ms>   格式：<0h:0m:0s:156ms>

		数据量是:10000  数据范围是:0~3
		##quick     :||耗时：<94ms>   格式：<0h:0m:0s:94ms>
		##bubble     :||耗时：<468ms>   格式：<0h:0m:0s:468ms>
		##insert     :||耗时：<94ms>   格式：<0h:0m:0s:94ms>
		##select     :||耗时：<74ms>   格式：<0h:0m:0s:74ms>
		##Shell      :||耗时：<312ms>   格式：<0h:0m:0s:312ms>

		数据量是:10000  数据范围是:0~100000
		##quick     :||耗时：<32ms>   格式：<0h:0m:0s:32ms>
		##bubble     :||耗时：<578ms>   格式：<0h:0m:0s:578ms>
		##insert     :||耗时：<186ms>   格式：<0h:0m:0s:186ms>
		##select     :||耗时：<578ms>   格式：<0h:0m:0s:578ms>
		##Shell      :||耗时：<531ms>   格式：<0h:0m:0s:531ms>

		这种情况下，速度是不确定的要看数据，几乎都是0ms，只有几个算法是需要耗时的
		数据量是:100  数据范围是:0~10000000

 * 初步结论：
 * 			     快速排序：好O(n*log n)，坏O(n^2)（基本有序的数据）
 *             冒泡的速度是稳定的，慢的，
 *             插入当极差小时速度快,基本有序时是近似线性的，O(n)
 *             选择 极差小更快
 *             Shell是受制于Java的类型转换，实现机制
 *
 *             急需使用多线程来加速，不然大数据量下的排序实在是太慢了
 * @author kcp
 */
public class TestSortTime {

    //这里来控制数据量大小
	private static int MOUNT;
    //数据范围
	private static int SCOPE;
    //参与排序的方法数
	private static int SORTDATA;
    //是否展示排序前后数据
	private static boolean display;

	public static void main(String [] args){
		TestSortTime.setMOUNT(100);
		TestSortTime.setSCOPE(10000000);
		TestSortTime.setSORTDATA(5);
		TestSortTime.setDisplay(false);

		System.out.println("数据量是:"+MOUNT+"\n数据范围是:0~"+SCOPE);
		GetRunTime time = new GetRunTime();
		List<int []> data = createDat();

//		display(data.get(0));//展示初始数据
		TestQuick(data.get(0), time, display);
		TestBubble(data.get(1), time, display);
		TestInsert(data.get(2), time, display);
		TestSelect(data.get(3), time, display);
		TestShell(data.get(4), time, display);
	}
	/**
	 * 创建一个List，里面的int[] 数据是相同的，但是内存地址不一样
	 * @return List<int []>
	 */
	public static List<int []> createDat(){
		List<int []> data = new ArrayList<int []>();
		/**创建数据容器*/
		for (int i=0;i<SORTDATA;i++){
			int [] dat1 = new int [MOUNT];
			data.add(dat1);
			//这里虽然是同一个引用，但是是不同的内存，所以数据是相互独立的，不会出现同一个的情况
			//但是如果是同一个内存，执行多次add，List会直接引用第一次add的地址，不会真正的add进去
//			data.add(new int[MOUNT]);//不能直接写一个new 要有引用指向它
		}
		/**产生大量数据*/
		for (int i=0;i<MOUNT;i++){
			int temp = (int)(Math.random()*SCOPE);
			//强制类型转换成int 后就全部变成0了？？？
			//知道原因了，是要对 运算结果 强制类型转换，对随机数强转int当然是0了，因为随机数是0到1
			for(int j=0;j<SORTDATA;j++){
				data.get(j)[i] = temp;
			}
		}
		return data;
	}

	/**测试快速排序*/
	public static void TestQuick(int [] dat, GetRunTime time, boolean display ){
//		if(display){System.out.print("未排序          ");display(dat);}
		time.Start();
		Quick.sort(dat,0,dat.length-1);
		time.End("\n##quick     :");
		if(display){System.out.println("已排序");display(dat);}
	}
	/**测试冒泡排序*/
	public static void TestBubble(int [] dat, GetRunTime time, boolean display ){
//		if(display){System.out.print("未排序          " );display(dat);}
		time.Start();
		Bubble.sort(dat);
		time.End("\n##bubble     :");
		//for (int i =0;i<dat.length;i++)
			//System.out.println(dat[i]);
		if(display){System.out.println("已排序");display(dat);}
	}
	/**测试插入排序*/
	public static void TestInsert(int [] dat, GetRunTime time, boolean display ){
//		if(display){System.out.print("未排序      ");display(dat);}
		time.Start();
		Insert.sort(dat);
		time.End("\n##insert     :");
		//for (int i =0;i<dat.length;i++)
			//System.out.println(dat[i]);
		if(display){System.out.println("已排序");display(dat);}
	}
	/**测试选择排序*/
	public static void TestSelect(int [] dat, GetRunTime time, boolean display ){
//		if(display){System.out.print("未排序     ");display(dat);}
		time.Start();
		Select.sort(dat);
		//for (int i =0;i<dat.length;i++)
			//System.out.println(dat[i]);
		time.End("\n##select     :");
		if(display){System.out.println("已排序");display(dat);}
	}
	private static void TestShell(int[] dat, GetRunTime time, boolean display){
//		if(display){System.out.print("未排序     ");display(dat);}
		time.Start();
		Shell.sort(dat);
		time.End("\n##Shell      :");
		if(display){System.out.println("已排序");display(dat);}
	}
	private static void display(int[] dat){
		for (int i =0;i<dat.length;i++) {
			System.out.print(dat[i]+"|");
			if(i%46==45){
			    System.out.println();
            }
//			if(i>100) break;
		}
	}

	public static int getMOUNT() {
		return MOUNT;
	}

	public static void setMOUNT(int mOUNT) {
		MOUNT = mOUNT;
	}

	public static int getSCOPE() {
		return SCOPE;
	}

	public static void setSCOPE(int sCOPE) {
		SCOPE = sCOPE;
	}

	public static int getSORTDATA() {
		return SORTDATA;
	}

	public static void setSORTDATA(int sORTDATA) {
		SORTDATA = sORTDATA;
	}

	public static boolean isDisplay() {
		return display;
	}

	public static void setDisplay(boolean display) {
		TestSortTime.display = display;
	}
}

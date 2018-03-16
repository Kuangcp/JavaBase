package com.github.kuangcp.sort;

/**
 * 冒泡排序，从小到大
 * 最坏的情况：O(n^2)
 * @author kcp
 */
public class Bubble {
	public static void sort (int [] arr){
		//冒泡循环次数
		for (int i=1;i<arr.length-1;i++){
			//用来冒泡的语句，0到已排序的部分
			for(int j=0;j<arr.length-i;j++){
				//大就交换，把最大的沉入最后
				if(arr[j]>arr[j+1]){
					int temp = arr[j+1];
					arr[j+1] = arr[j];
					arr[j] = temp;
				}
			}
		}
	}

}

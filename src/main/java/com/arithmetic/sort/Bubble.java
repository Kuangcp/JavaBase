package com.arithmetic.sort;

/**
 * 冒泡排序，从小到大
 * 最坏的情况：O(n^2)
 */
public class Bubble {
	public static void sort (int [] arr){
		for (int i=1;i<arr.length-1;i++){//冒泡循环次数
			for(int j=0;j<arr.length-i;j++){//用来冒泡的语句，0到已排序的部分
				if(arr[j]>arr[j+1]){//大就交换，把最大的沉入最后
					int temp = arr[j+1];
					arr[j+1] = arr[j];
					arr[j] = temp;
				}
			}
		}
	}

}

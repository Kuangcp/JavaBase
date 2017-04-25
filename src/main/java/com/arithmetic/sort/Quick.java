package com.arithmetic.sort;
/**
 * 快速排序
 * 最坏的情况：当数列是一样的数据 O(n^2)
 * 理想：O(n*log n)
 * @author Myth
 * @date 2016年10月29日 下午1:10:36
 */
public class Quick {
	public static void sort(int arr[], int low, int high) {
		int l = low;
		int h = high;
		int povit = arr[low];

		while (l < h) {
			while (l < h && arr[h] >= povit)
				h--;
			if (l < h) {
				int temp = arr[h];
				arr[h] = arr[l];
				arr[l] = temp;
				l++;
			}

			while (l < h && arr[l] <= povit)
				l++;

			if (l < h) {
				int temp = arr[h];
				arr[h] = arr[l];
				arr[l] = temp;
				h--;
			}
		}
//		System.out.printf("l=" + (l + 1) + "h=" + (h + 1) + "povit=" + povit+ "\n");
		if (l > low)
			sort(arr, low, l - 1);
		if (h < high)
			sort(arr, l + 1, high);
	}
//*************************************************************************
	// 更高效点的代码：
	public  <T extends Comparable<? super T>> T[] quickSort(T[] targetArr,
			int start, int end) {
		int i = start + 1, j = end;
		T key = targetArr[start];
		// SortUtil<T>sUtil=newSortUtil<T>();

		if (start >= end)
			return (targetArr);

		/*
		 * 从i++和j--两个方向搜索不满足条件的值并交换
		 * 
		 * 条件为：i++方向小于key，j--方向大于key
		 */
		while (true) {
			while (targetArr[j].compareTo(key) > 0)
				j--;
			while (targetArr[i].compareTo(key) < 0 && i < j)
				i++;
			if (i >= j)
				break;
			// sUtil.swap(targetArr,i,j);
			if (targetArr[i] == key) {
				j--;
			} else {
				i++;
			}
		}

		/* 关键数据放到‘中间’ */
		// sUtil.swap(targetArr,start,j);

		if (start < i - 1) {
			this.quickSort(targetArr, start, i - 1);
		}
		if (j + 1 < end) {
			this.quickSort(targetArr, j + 1, end);
		}

		return targetArr;
	}
}

// /*//////////////////////////方式二////////////////////////////////*/

/* //////////////方式三：减少交换次数，提高效率///////////////////// */
// private<T extends Comparable<? super T>>
// void quickSort(T[]targetArr,intstart,intend)
// {
// int i=start,j=end;
// Tkey=targetArr[start];
//
// while(i<j)
// {
// /*按j--方向遍历目标数组，直到比key小的值为止*/
// while(j>i&&targetArr[j].compareTo(key)>=0)
// {
// j--;
// }
// if(i<j)
// {
// /*targetArr[i]已经保存在key中，可将后面的数填入*/
// targetArr[i]=targetArr[j];
// i++;
// }
// /*按i++方向遍历目标数组，直到比key大的值为止*/
// while(i<j&&targetArr[i].compareTo(key)<=0)
// /*此处一定要小于等于零，假设数组之内有一亿个1，0交替出现的话，而key的值又恰巧是1的话，那么这个小于等于的作用就会使下面的if语句少执行一亿次。*/
// {
// i++;
// }
// if(i<j)
// {
// /*targetArr[j]已保存在targetArr[i]中，可将前面的值填入*/
// targetArr[j]=targetArr[i];
// j--;
// }
// }
// /*此时i==j*/
// targetArr[i]=key;
//
// /*递归调用，把key前面的完成排序*/
// this.quickSort(targetArr,start,i-1);
//
//
// /*递归调用，把key后面的完成排序*/
// this.quickSort(targetArr,j+1,end);
//
// }

/*
 * public class Quick { //快速排序：一堆的数组下标越界
 * 
 * void quick (int []Q,int left,int right) { int left_x=left; int right_x=right;
 * int pivot=Q[(left+right)/2]; while (left_x<=right_x) {
 * for(;Q[left_x]<pivot;left_x++);//从左边开始查找 for(;Q[right_x]>pivot &&
 * right_x>0;right_x--);//从右边开始查找 if (left_x<=right_x)//执行交换 { if
 * (left_x!=right_x) {int temp =
 * Q[left_x];Q[right_x]=Q[left_x];Q[right_x]=temp;}
 * //SWAP(Q[left_x],Q[right_x]);//怎么实现交换 left_x++;right_x--; } } if
 * (right_x>left)//函数的递归调用 { quick(Q,left,right_x); } if (left_x<right)//函数的递归调用
 * { quick(Q,left_x,right); } } void quick_all(int []Q,int
 * data_size)//检查全局，再次递归调用 { quick(Q,0,data_size-1); } public void
 * sort(int[]arr){ quick(arr,0,arr.length-2); //指定位置进行排序
 * quick_all(arr,arr.length-1); //只检查一遍就够了？ } public static void main(String []
 * args) { int data[7],i; for (i=0;i<7;i++) { printf("输入数据");
 * scanf("%d",&data[i]); } quick(data,0,6);//指定位置进行排序
 * quick_all(data,7);//只检查一遍就够了？ for (i=0;i<7;i++) printf ("%2d\n",data[i]); }
 * //程序没错误，原理已经清楚了
 * 
 * }
 */
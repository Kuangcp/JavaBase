package com.arithmetic.sort;

import java.util.ArrayList;
import java.util.List;

/**
 * shell排序，从小到大
 * 算法思想：
 * 	对一个线性数据，先取一个随机数d1（d1<数据总数）将所有数据分成d组，将所有距离为d倍数的分成一组
 *  对各组进行直接插入排序，然后再取第二个数d2，直到dn=1（dn<……d3<d2<d1）即分成了一组，再插入排序，得出最终结果
 * 时间复杂度：平均：O(n*log n) 最坏 O(n^s)(1<s<2)
 *
 * 实现了算法，但是大量的数据转换，消耗了性能，需要换种思路来实现算法
 * @author  Myth
 * @date 2016年10月29日 下午5:25:14
 */
public class Shell {
	public static void sort (int [] arr){
		//如果改变了引用的指向，是对调用者无效的，要改变该引用的内存上的数据
//		Test.display(arr);
	
		List<List<Integer>> datalist  = new ArrayList<List<Integer>>();
		List <Integer> arrs= new ArrayList<Integer>();
		for (int i=0;i<arr.length;i++){
			arrs.add(arr[i]);
		}
		datalist.add(arrs);
		int length = arr.length;
		int lastd=length,d = 0;
		
		boolean flag = true;
		while(flag){
			d = (int)(Math.random()*lastd-1)+1;//取得随机数.比上一次取得值要小
//			System.out.println("\n随机数 :  "+d);
			List<List<Integer>> temp  = new ArrayList<List<Integer>>();
//			int index = 0;
			//将数据分组
			for(int i=0;i<d;i++){
				//一组的内存
				List<Integer> dat = new ArrayList<Integer>();//分配内存
				
//				index=0;
//				temp.add(dat);
				for(int j=0;j<datalist.size();j++){
					List<Integer> temps = datalist.get(j);
					for(int k=i;k<temps.size();k+=d){
//						System.out.println(temps.get(k));
						if(temps.get(k)!=null)dat.add(temps.get(k));
					}
				}
				int [] arrdat = new int[dat.size()];
				for(int h=0;h<dat.size();h++){
					arrdat[h] = dat.get(h);
				}
				Insert.sort(arrdat);//直接插入排序
				if(d==1){
					for(int h=0;h<arr.length;h++){
						arr[h] = arrdat[h];
					}
					flag=false;
				}
//				TestSortTime.display(arrdat);
				temp.add(dat);//加入集合
//				System.out.println("缓存数组内存长度:"+dat.size());
				
			}
			datalist =  temp;
			lastd = d;
		}
	}
}

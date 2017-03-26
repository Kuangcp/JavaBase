package com.arithmetic.found;


import com.arithmetic.sort.Insert;

public class test_1 {
	public static void main (String []args){
		Insert insert = new Insert();
		ErFenFa s = new ErFenFa();
		
		int [] dat = new int [5000];
		for (int i=0;i<dat.length;i++){
			dat[i] =(int)(Math.random()*90000+10);}
        insert.sort(dat);
        
        for (int i =0;i<dat.length;i++) {  //将数组遍历一下
			System.out.print(dat[i]+" ");
			if ((i+1)%10 == 0) System.out.println();
        }
		System.out.println();
		
		int result = s.find(dat, 58);
		if (result!=-1)
		System.out.println("你要找的数据在第"+result+"个位置");
		else System.out.println("该数据不存在，查找失败！");
		
	}

}

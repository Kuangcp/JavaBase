package com.github.kuangcp.strcuture.tree;

import java.util.Scanner;

/**
 * Created by https://github.com/kuangcp on 17-8-22  下午3:08
 */

public class BiTree {
    public BiTree root;
    public Object data;
    public BiTree lc;
    public BiTree rc;

    Scanner sc = new Scanner(System.in);
    public void Initiate(BiTree bt){//初始化二叉树
        bt.lc = bt.rc =null;
    }

	/*public Bitree Create(Bitree bt){        这是啥  ？建立一堆 无关系的节点干啥？
		Object data = sc.next();
		if (data==".") bt=null;
		else {
			bt = new Bitree();
			bt.data = data;
		}
		return bt;
	}*/

    public void Destory(BiTree bt){
        bt=null;
    }

    public boolean  Empty(BiTree bt){
        return bt.lc ==null && bt.rc== null;
    }

    public Object Root(BiTree bt){
        return bt.data;
    }

    public BiTree Parent(BiTree bt, Object x){ //  求父亲节点
        return bt;
    }
    public BiTree(String preOrder, String inOrder, int preIndex, int inIndex, int count ){
        if (count >0){   //先根和中根非空
            char r =preOrder.charAt(preIndex);//取先根遍历序列中的第一个节点作为根节点
            int i = 0;
            for (;i<count;i++)
                if (r == inOrder.charAt(i+inIndex))  break ;
            //root = new BitreeNode(r);
        }


    }
    public void  Traverse(BiTree bt){//  先序遍历二叉树
        if (bt!=null){
            System.out.println(Root(bt));
            Traverse(bt.lc);
            Traverse(bt.rc);
        }
    }

}


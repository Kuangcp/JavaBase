package com.questions;
import java.util.*;
/**
 * 给出一组数字，只有加减，给你一个数字判断是否能从数组中计算得到
 * 最好列出等式
 * 解决，耗时一个多小时，两个月没有码代码，大脑迟钝太多了！
 * Created by l on 2017/1/3
 */
public class ListDengShi {
    static int[] datas;
    static List<String>result = new ArrayList<String>();
    static Map<String,String> results = new HashMap<String,String>();
    static int num;//记录数据大小


    public static void main(String[]a){
        datas = new int[]{6,3,2,4,8,2,1,2,1,3,2};
        num = datas.length;

        ListDengShi test = new ListDengShi();
        test.caculateFuHao("",0);
        test.caculateResult(result);

        System.out.println("组合结果数 ："+result.size());
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入查询结果");
        String target = sc.nextLine();
        System.out.println(results.getOrDefault(target, "没有该结果"));
    }

    /**
     * 递归求出可能的等式
     * @param fuhao
     * @param index
     */
    public void caculateFuHao (String fuhao,int index){
        if(index<num) {
//            System.out.println("fuhao:"+fuhao+" index:"+index);
            index++;
            caculateFuHao(fuhao +"+",index);
            caculateFuHao(fuhao +"-",index);
        }else{
            result.add(fuhao);
        }
    }

    /**
     * 根据等式计算出值
     * @param dengshi
     */
    public void caculateResult(List<String> dengshi){
        for (String row : dengshi) {
            if (row.length() >= num) {
                int temp = 0;
                StringBuilder buffer = new StringBuilder();
//            System.out.println("fuhao 长度："+row.length());
                for (int j = 0; j < row.length(); j++) {
                    String fuhao = row.charAt(j) + "";
                    if ("+".equals(fuhao)) {
                        temp += datas[j];
//                        System.out.print("temp:"+temp);
                    } else {
                        temp -= datas[j];
                    }
                    buffer.append(fuhao).append(datas[j]);
//                    System.out.print(fuhao +""+datas[j]);
                }
                buffer.append(" = ").append(temp);
//                System.out.println(" = " + temp);
                results.put(temp + "", buffer.toString());
            }
        }
    }
    public void dis(List<String> lists){
        for(String d:lists){
            System.out.println(d);
        }
    }

}

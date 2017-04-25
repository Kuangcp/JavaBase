package com.math.SimplexMethodQuarter;

import com.math.number.Quarter;
import com.util.file.ReadProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Myth on 2017/3/22 0022
 */
public class SimplexMethod {

    private static ReadProperties config;//读取配置文件
    private static int MAXPARAMS;//最大参数个数
    private static int EQUALITY;//最大行列式数
    private List<Equality> Rows = new ArrayList<Equality>();//约束式子系数集合
    private List<Quarter>Max = new ArrayList<Quarter>();//原始式子系数集合
    private List<Table> Tables = new ArrayList<Table>();//单纯形表的总体数据结构
    private List<Quarter> Os = new ArrayList<Quarter>();//单纯形表中间计算结果右侧
    private List<Quarter> Zs = new ArrayList<Quarter>();//单纯形表计算结果最下一行
    //出入基锁定的坐标
    private Integer resultCol;
    private Integer resultRow;
    private boolean CONTINUE = true;//记录是否要继续计算的标识属性
    private boolean SUCCESS = true;//记录是否成功计算出结果
    private Map<String,String> Xbs= new HashMap<String,String>();
    private Integer Round=1;

    static{
        config = new ReadProperties("src/main/resources/math/SimplexMethod.properties");
        MAXPARAMS = config.getInt("MaxParams");
        EQUALITY = config.getInt("Equality");
    }
    public static void main(String[]s){
        SimplexMethod sm = new SimplexMethod();
        try{
            sm.run();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 初始化整体数据
     */
    public void init(){
        //添加求解式的数据
        String maxs = config.getString("Max");
        String [] temp = maxs.split(",");
        for(String data:temp){
//            System.out.println(data);
            Max.add(new Quarter(data));
        }
        //添加约束式的数据
        for(int i=1;i<=EQUALITY;i++){
            String buffer = config.getString("E"+i);
            String[] tempList = buffer.split(",");
            Equality e = new Equality();
            for(int j=0;j<tempList.length;j++){
                e.getParams().add(new Quarter(tempList[j]));
            }
            e.setResult(new Quarter(config.getString("B"+i)));
            e.setIndex(config.getInt("I"+i));
            Rows.add(e);

        }
        //填入单纯形表总体数据中去
        Equality stRow;
        for(int i=0;i<EQUALITY;i++){
            stRow = Rows.get(i);
            Integer index = stRow.getIndex();
            //装填第一行的数据
            Table table = new Table(Max.get(index-1),index,stRow.getResult(),stRow.getParams(),null);
            Tables.add(table);
        }
        //查看数据是否正确装填
//        disp("单纯形表的中心数据 : ",Tables);
        disp("求解的方程式系数 ：",false,Max,false);
//        disp("约束条件的方程式",Rows);
        //展示方程式
        System.out.println("原题样式 : ");
        showRows();

    }

    /**
     * 循环计算直到出现结果
     */
    public void run()throws Exception{
        init();
//运算出初始的表格
        disp("中间计算结果",Tables);
        //计算最底下一行
        CaculateLastRow();
        //计算右边列
        CaculateRightCol();
        //判断是否达到退出条件
        CONTINUE = isCONTINUES(Zs);

    //迭代的计算直到满足条件
        while(CONTINUE){
            try {
                Thread.sleep(100);
            }catch (Exception e ){
                e.printStackTrace();
            }
            //算完一轮
            Quarter fatherNum = Tables.get(resultRow).getRows().get(resultCol);
            List<Table> oldTables = Tables;
            Tables = new ArrayList<Table>();

            //转换出基入基的参数
            List<Quarter> rowsTemps= oldTables.get(resultRow).getRows();
            for(int k=0;k<rowsTemps.size();k++){
                rowsTemps.set(k,rowsTemps.get(k).divide(fatherNum));
            }
            Table temp = new Table(Max.get(resultCol),resultCol+1,oldTables.get(resultRow).getBl().divide(fatherNum),rowsTemps,null);
            Tables.add(temp);
            //转换剩余的
            for(int j=0;j<EQUALITY;j++){
                if(j==resultRow){
                    continue;
                }else{
                    rowsTemps = oldTables.get(j).getRows();
                    Quarter motherNum = rowsTemps.get(resultCol);
                    Table fatherRow = Tables.get(0);
                    for(int k=0;k<rowsTemps.size();k++){
                        rowsTemps.set(k,rowsTemps.get(k).reduce(motherNum.multi(fatherRow.getRows().get(k))));
                    }
                    Integer tempXb = oldTables.get(j).getXb();
                    Table otherTemp = new Table(Max.get(tempXb-1),tempXb,oldTables.get(j).getBl().reduce(motherNum.multi(fatherRow.getBl())),rowsTemps,null);
                    Tables.add(otherTemp);
                }

            }
//            System.out.println("Tables"+Tables.size());
            Zs.clear();
            Os.clear();
//            log("下右两个计算集合的大小"+Zs.size()+":"+Os.size());

            disp("中间计算结果",true,Tables,true);
            //计算最后一行
            CaculateLastRow();
            //判断是否达到退出条件
            CONTINUE = isCONTINUES(Zs);
            if(CONTINUE) {
                CaculateRightCol();
            }
            System.out.println("******************************");
        }

        //运算完成
        //@TODO 要进行判断是否成功计算，然后执行不同的方法
        FinallyResult();
    }

    /**
     * 处理最后运行结果，进行判断
     * 1. 右列没有一个正数，最后一行也没有正数
     * 2. Xb列死循环，即变量循环的出基入基
     * @throws Exception
     */
    public void FinallyResult() throws Exception{
        Integer RightMin = MaxList(Os,false,true,false);
        if(RightMin==-1){
            System.out.println("右列没有一个正数，最后一行也没有正数，原方程没有最优解");
            SUCCESS=false;
        }else{
            SUCCESS=true;
        }
        //正确的计算出结果
        if(SUCCESS) {
            Quarter result = new Quarter(0);
            Quarter[] results = new Quarter[MAXPARAMS];
            for (int i = 0; i < EQUALITY; i++) {
                Table t = Tables.get(i);
                result = result.plus(t.getCb().multi(t.getBl()));
                results[t.getXb() - 1] = t.getBl();
            }
            System.out.println("最优目标值是 : " + result);
            String resultstr = "X=(";
            for (Quarter d : results) {
                if (d != null) {
                    resultstr += d + ",";
                } else {
                    resultstr += "0,";
                }
            }
            resultstr = resultstr.substring(0, resultstr.length() - 1);
            resultstr += ")";
            System.out.println(resultstr);

        }
        for(String key :Xbs.keySet()){
            log(Xbs.get(key)+"轮"+key);
        }
    }
    /**
     * 计算最后一行和最右边的一列
     */
    public void CaculateLastRow()throws Exception{
        for(int i=0;i<MAXPARAMS;i++){//循环变量个数
            Quarter temp = Max.get(i);
//            disp("目标方程系数组",Max,false);
//            System.out.println("Max中取到的temp"+temp);
            for(int j=0;j<EQUALITY;j++){//循环层数
//                System.out.println("乘积 "+Tables.get(j).getCb()+"*"+Tables.get(j).getRows().get(i)+" = "+Tables.get(j).getCb().multi(Tables.get(j).getRows().get(i)));
                temp = temp.reduce(Tables.get(j).getCb().multi(Tables.get(j).getRows().get(i)));
//                System.out.println("temp:"+temp+"Cb:"+Tables.get(j).getCb()+"*"+Tables.get(j).getRows().get(i));
            }
            Zs.add(temp);
//            System.out.println("jieguo "+temp);
        }
        disp("最后一行",false,Zs,false);
        resultCol = MaxList(Zs,true,true,true);

    }
    //计算右边栏
    public void CaculateRightCol()throws Exception{

//        log("计算所得行最大Index"+resultCol);
        for(int i=0;i<EQUALITY;i++){
//            log("计算表达式 "+Tables.get(i).getBl()+" / "+Tables.get(i).getRows().get(resultCol));
            Quarter temp = Tables.get(i).getBl().divide(Tables.get(i).getRows().get(resultCol));
//            log("结果"+temp);
            Tables.get(i).setO(temp);
            Os.add(temp);
        }
        disp("右栏",false,Os,false);
        resultRow = MaxList(Os,false,true,false);
        if(resultRow==-1) CONTINUE=false;
//        log("右边最小index ："+resultRow);
        // @ToDo 计算右边当右边有大于0的数 才继续，否则退出计算 要结合退出函数使用

    }
    /**
     * 1.判断最后一行是否到了最后的计算结果   即全小于0 就可以退出计算了
     * 2.又加入一个判断机制，循环出入基的情况
     *
     * @param list
     * @return false就不再继续
     */
    public boolean isCONTINUES(List<Quarter> list){
        boolean flag = false;
        for(Quarter b:list){
            if(b.upZero()){
                flag=true;
                break;
            }
        }
//        disp("检测",Tables);
        String temp = "";
        for(int i=0;i<EQUALITY;i++){
            temp +=Tables.get(i).getXb();
        }
        if(!Xbs.containsKey(temp)){
            Xbs.put(temp,Round+++"");
        }else{
            return false;
        }
        return flag;

    }

    /**
     * 求解含有非数的集合的极值，异常返回-1
     * @param list
     * @param isMax
     * @param haveInfinity
     * @param permitMinus 是否允许负数进行笔记比较
     * @return
     */
    public Integer MaxList(List<Quarter> list,boolean isMax,boolean haveInfinity,boolean permitMinus){
        Integer index=null;
        //有非数的集合
        if(haveInfinity){
            Map<Integer,Quarter> tempMap = new HashMap<Integer,Quarter>();
            //去除非数
            for(int i=0;i<list.size();i++){
                if(permitMinus&&list.get(i).getDenominator()!=0){
                    tempMap.put(i,list.get(i));
                }
                //不允许负数的情况
                if(!permitMinus){
                    if(list.get(i).getDenominator()!=0 && list.get(i).upZero()){
                        tempMap.put(i,list.get(i));
                    }
                }
            }
            if(tempMap.size()==0){
                return -1;
            }
            for (Integer integer :tempMap.keySet()) {
                if(index==null){
                    index = integer;
                    continue;
                }else if(isMax && !tempMap.get(index).bigger(tempMap.get(integer))){
                    index = integer;
                }else if(!isMax && tempMap.get(index).bigger(tempMap.get(integer))){
                    index = integer;
                }
            }
        }
        //没有非数的集合
        if(!haveInfinity){
            if(list.size()==0){
                return -1;
            }
            Quarter temp = list.get(0);
            for(int i=0;i<list.size();i++){
                if(list.get(i).upZero()){
                    if(isMax && !temp.bigger(list.get(i))){
                        index=i;
                        temp = list.get(i);
                    }
                    if(!isMax && temp.bigger(list.get(i))){
                        index=i;
                        temp = list.get(i);
                    }
                }

            }

        }
        return index;
    }

    /**
     * 方便展示原始数据
     * @param list
     */
    public void disp(String title,List list){
        disp(title,true,list,true);
    }

    /**
     * 展示数据
     * @param title
     * @param titleTurn 标题是否换行
     * @param list
     * @param turn 内容是否换行
     */
    public void disp(String title,boolean titleTurn,List list,boolean turn){
        if(titleTurn){
            System.out.println(title);
        }else{
            System.out.print(title+" :    ");
        }
        for(int i=0;i<list.size();i++){
            if(turn){
                System.out.println(list.get(i).toString());
            }else{
                System.out.print(list.get(i).toString()+"    ");
            }
        }
        if(!turn) System.out.println();
    }

    /**
     * 展示整体方程式,原题的样子
     */
    public void showRows(){
        String MaxRows="Max(z)=";
        for(int i=0;i<MAXPARAMS;i++){
            if(!Max.get(i).isZero())MaxRows+=Max.get(i)+" X"+(i+1)+" + ";
        }
        MaxRows = MaxRows.substring(0,MaxRows.length()-2);
        System.out.println("Aim : "+MaxRows);
        for(int i=0;i<EQUALITY;i++){
            String Row="";
            List<Quarter> temp = Rows.get(i).getParams();
            for(int j=0;j<temp.size();j++){
                Quarter a = temp.get(j);
                if(!a.isZero()){
                    if(!a.isOne()){
                        Row+=a.toString()+"X"+(j+1)+" + ";
                    }else{
                        Row+="X"+(j+1)+" + ";
                    }
                }else{
                    Row+="      ";
                }
            }
            Row = Row.substring(0,Row.length()-2);
            Row+=" = "+Rows.get(i).getResult() +" |X"+Rows.get(i).getIndex()+"|";
            System.out.println("Equality : "+Row);
        }
    }
    public void log(String s){
        System.out.println(s);
    }
}

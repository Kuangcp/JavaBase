package com.github.kuangcp.simple;

import java.io.Serializable;

/**
 * Created by https://github.com/kuangcp on 18-1-12  下午5:23
 *  具体的实现类,用于测试
 * @author kuangcp
 */

public class Score implements Comparable, Serializable{

    private Float normal; // 平时成绩
    private Float terminal;// 期末成绩

    /**
     * 这个方法一般是 -1 0 1: 本对象 小于 等于 大于 参数对象 或者 负数 0 正数
     * @param o 要比较的对象 同类之间的比较
     * @return int类型
     */
    @Override
    public int compareTo(Object o) {
        double temp = this.normal*0.3+this.terminal*0.7;
        double target = ((Score) o).normal*0.3+((Score) o).terminal*0.7;
        if (temp == target){
            return 0;
        }else if(temp>target){
            return 1;
        }
        return -1;
    }

    public Score(Float normal, Float terminal) {
        this.normal = normal;
        this.terminal = terminal;
    }

    public Float getNormal() {
        return normal;
    }

    public void setNormal(Float normal) {
        this.normal = normal;
    }

    public Float getTerminal() {
        return terminal;
    }

    public void setTerminal(Float terminal) {
        this.terminal = terminal;
    }
}

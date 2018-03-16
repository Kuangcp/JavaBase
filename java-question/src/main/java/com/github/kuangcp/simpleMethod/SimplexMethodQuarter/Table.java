package com.github.kuangcp.simpleMethod.SimplexMethodQuarter;


import com.github.kuangcp.simpleMethod.number.Quarter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Myth on 2017/3/22
 * 使用分数作为基本数据类型来计算
 */
public class Table {
    private Quarter cb;
    private Integer xb;
    private Quarter bl;
    private List<Quarter> rows;
    private Quarter o;

    public Table(){}

    public Table(Quarter cb, Integer xb, Quarter bl, List<Quarter> rows, Quarter o) {
        this.cb = cb;
        this.xb = xb;
        this.bl = bl;
        this.o = o;
        this.rows = new ArrayList<Quarter>();
        for (Quarter quarter : rows) {
            this.rows.add(quarter);
        }
//        System.out.println(this.toString());
    }

    public Quarter getCb() {
        return cb;
    }

    public void setCb(Quarter cb) {
        this.cb = cb;
    }

    public Integer getXb() {
        return xb;
    }

    public void setXb(Integer xb) {
        this.xb = xb;
    }

    public Quarter getBl() {
        return bl;
    }

    public void setBl(Quarter bl) {
        this.bl = bl;
    }

    public List<Quarter> getRows() {
        return rows;
    }

    public void setRows(List<Quarter> rows) {
        this.rows = rows;
    }

    public Quarter getO() {
        return o;
    }

    public void setO(Quarter o) {
        this.o = o;
    }

    @Override
    public String toString() {
        String sub="[";
        for(Quarter b:rows){
            sub+=b.toString()+",";
        }
        sub+="]";
        return "Table{" +
                "cb=" + cb +
                ", xb=" + xb +
                ", bl=" + bl +
                ", rows=" + sub +
                ", o=" + o +
                '}';
    }
}

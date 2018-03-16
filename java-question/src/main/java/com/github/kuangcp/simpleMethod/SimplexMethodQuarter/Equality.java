package com.github.kuangcp.simpleMethod.SimplexMethodQuarter;


import com.github.kuangcp.simpleMethod.number.Quarter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Myth on 2017/3/22
 * 约束式的对象形式 分数数据类型
 */
public class Equality {
    Integer index;
    List<Quarter> params;
    Quarter result;

    public Equality(){
        params = new ArrayList<Quarter>();
    }

    public Equality(List<Quarter> params, Quarter result) {
        this.params = params;
        this.result = result;
    }

    public Equality(Integer index, List<Quarter> params, Quarter result) {
        this.index = index;
        this.params = params;
        this.result = result;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public List<Quarter> getParams() {
        return params;
    }

    public void setParams(List<Quarter> params) {
        this.params = params;
    }

    public Quarter getResult() {
        return result;
    }

    public void setResult(Quarter result) {
        this.result = result;
    }

    @Override
    public String toString() {
        String sub="[";
        for(Quarter b:params){
            sub+=b.toString()+",";
        }
        sub+="]";
        return "Equality{" +
                "index=" + index +
                ", params=" + sub +
                ", result=" + result +
                '}';
    }
}

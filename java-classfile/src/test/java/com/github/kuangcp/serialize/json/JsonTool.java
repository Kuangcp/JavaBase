package com.github.kuangcp.serialize.json;

import com.github.kuangcp.serialize.binary.Person;

import java.util.List;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 * @date 18-5-30  下午6:02
 */
public interface JsonTool {

    void read();
    void write(int total, List<Person> dataList);
    String getName();

}

package com.github.kuangcp.serialize.json;

import com.github.kuangcp.serialize.binary.Person;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 * @date 18-5-30  下午5:55
 */
public class GsonTest implements JsonTool {

    @Override
    public void read() {

    }

    @Override
    public void write(int total, List<Person> dataList) {
        Gson gson = new Gson();
        gson.toJson(dataList);
//        System.out.println(result);

    }

    @Override
    public String getName() {
        return "GSON";
    }
}

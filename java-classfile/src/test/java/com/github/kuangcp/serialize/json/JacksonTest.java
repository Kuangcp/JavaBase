package com.github.kuangcp.serialize.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kuangcp.serialize.Person;
import org.ho.yaml.wrapper.ObjectWrapper;

import java.util.List;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 * @date 18-5-30  下午5:55
 */
public class JacksonTest implements JsonTool {

    @Override
    public void read() {

    }

    @Override
    public void write(int total, List<Person> dataList) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValueAsString(dataList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "Jackson";
    }
}

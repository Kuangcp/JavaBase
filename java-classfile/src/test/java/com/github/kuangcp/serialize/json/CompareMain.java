package com.github.kuangcp.serialize.json;

import com.github.kuangcp.serialize.Person;
import com.myth.time.GetRunTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 * @date 18-5-30  下午5:59
 */
public class CompareMain {

    private GetRunTime run = new GetRunTime();
    private int writeTotal = 2000;

    private List<JsonTool> list = Arrays.asList(new GsonTest(), new JacksonTest(), new FastJsonTest());
    private List<Person> personList = new ArrayList<>();
    {
        for (int i = 0; i < writeTotal; i++) {
            Person person = new Person();
            person.setAddress("address"+i);
            person.setName("name"+i);
            person.setPhone("phone"+i);
            personList.add(person);
        }
    }

    @Test
    public void compareRead(){

    }

    @Test
    public void compareWrite(){
        for (JsonTool item : list) {
            run.startCount();
            item.write(writeTotal, personList);
            run.endCount(item.getName());
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }

    }
}

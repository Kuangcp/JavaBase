package com.net.socket.runable;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Myth on 2017/4/1
 * Map String的相互转换
 */
public class StringTest {
    private Logger logger = LoggerFactory.getLogger(StringTest.class);
    @Test
    public void testMap(){
        Map<String,String> map = new HashMap<String,String>();
        map.put("12","34");
        map.put("就是","没有");
        logger.info("输出"+map.toString());

    }
    @Test
    public void testInput() throws IOException {
        // 使用 BufferedReader 而不是 Scanner
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String res = br.readLine();
        logger.info(res);
    }

    @Test
    public void testSplit(){
        String test = "name: ui, age:100";
        String []s = test.split(":|,");
        for(String sss: s){
            System.out.println(sss);
        }
    }
}

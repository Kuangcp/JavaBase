package com.net.socket.runable;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Myth on 2017/4/1
 * Map String的相互转换
 */
public class StringTest {
    Logger logger = LoggerFactory.getLogger(StringTest.class);
    @Test
    public void testMap(){
        Map<String,String> map = new HashMap<String,String>();
        map.put("12","34");
        map.put("就是","没有");
        logger.info("输出"+map.toString());

    }
    @Test
    public void testInput(){

        Scanner  scanner = new Scanner(System.in);
        scanner.next();
        logger.info(scanner.nextLine());
    }
}

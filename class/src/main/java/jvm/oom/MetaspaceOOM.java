package jvm.oom;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author kuangcp
 * 2023-08-25 17:24
 */
@Slf4j
public class MetaspaceOOM {

    @Getter
    private static class Bucket {
        private int a;
        private int b;
        private String c;
        private int d;
        private String f0;
        private String f1;
        private String f2;
        private String f3;
        private String f4;
        private String f5;
        private String f6;
        private String f7;
        private String f8;
        private String f9;
        private String f10;
        private String f11;
        private String f12;
        private String f13;
        private String f14;
        private String f15;
        private String f16;
        private String f17;
        private String f18;
        private String f19;
        private String f20;
        private String f21;
        private String f22;
        private String f23;
        private String f24;
        private String f25;
        private String f26;
        private String f27;
        private String f28;
        private String f29;

        public List<List<String>> buildMap() {
            return Collections.emptyList();
        }
    }

    public static void jacksonAndReflect() throws Exception {
        final Bucket bucket = new Bucket();
        for (int j = 0; j < 100; j++) {
            log.info("run loop {}", j);
            TimeUnit.MILLISECONDS.sleep(350);
            for (int i = 0; i < 1000; i++) {
                final ObjectMapper mapper = new ObjectMapper();
                final Method buildMap = Bucket.class.getMethod("buildMap");

                buildMap.invoke(bucket);
                final String json = mapper.writeValueAsString(bucket);
                mapper.readValue(json, Bucket.class);
            }
        }
    }

}

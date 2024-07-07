package com.github.kuangcp.tank.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2024-07-07 11:50
 */
public class Json {

    private static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public static String toJson(Object obj) {
        return mapper.writeValueAsString(obj);
    }

    @SneakyThrows
    public static <T> T fromJson(String json, Class<T> clazz) {
        return mapper.readValue(json, clazz);
    }
}

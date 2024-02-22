package com.github.kuangcp.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

/**
 * @author kuangchengping@sinohealth.cn
 * 2023-06-20 18:42
 */
public class StrUtil {

    private static final List<String> LIST = Arrays.asList("A", "a", "B", "b", "C", "c", "D", "d", "E", "e", "F", "f",
            "G", "g", "H", "h", "I", "i", "J", "j", "K", "k", "L", "l", "M", "m", "N", "n", "O", "o", "P", "p", "Q", "q",
            "R", "r", "S", "s", "T", "t", "U", "u", "V", "v", "W", "w", "X", "x", "Y", "y", "Z", "z");

    private static final String[] ALPHA = new String[]{"A", "a", "B", "b", "C", "c", "D", "d", "E", "e", "F", "f",
            "G", "g", "H", "h", "I", "i", "J", "j", "K", "k", "L", "l", "M", "m", "N", "n", "O", "o", "P", "p", "Q", "q",
            "R", "r", "S", "s", "T", "t", "U", "u", "V", "v", "W", "w", "X", "x", "Y", "y", "Z", "z"};


    public static String firstNotBlankStr(String... names) {
        return Stream.of(names).filter(StringUtils::isNotBlank).findFirst().orElse("");
    }

    public static String randomAlpha(int len) {
        Random random = new Random();
        random.setSeed(System.nanoTime());
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(LIST.get(random.nextInt(LIST.size())));
        }
        return builder.toString();
    }

    public static String randomAlphaA(int len) {
        Random random = new Random();
        random.setSeed(System.nanoTime());
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(ALPHA[random.nextInt(LIST.size())]);
        }
        return builder.toString();
    }

    public static String randomAlphaAL(int len) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(ALPHA[random.nextInt(LIST.size())]);
        }
        return builder.toString();
    }
}

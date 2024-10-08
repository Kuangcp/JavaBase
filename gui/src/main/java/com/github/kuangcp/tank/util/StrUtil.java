package com.github.kuangcp.tank.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

/**
 * @author Kuangcp
 * 2023-06-20 18:42
 */
public class StrUtil {

    private static final List<String> LIST = Arrays.asList("A", "a", "B", "b", "C", "c", "D", "d", "E", "e", "F", "f",
            "G", "g", "H", "h", "I", "i", "J", "j", "K", "k", "L", "l", "M", "m", "N", "n", "O", "o", "P", "p", "Q", "q",
            "R", "r", "S", "s", "T", "t", "U", "u", "V", "v", "W", "w", "X", "x", "Y", "y", "Z", "z");

    private static final String[] ALPHA = new String[]{"A", "a", "B", "b", "C", "c", "D", "d", "E", "e", "F", "f",
            "G", "g", "H", "h", "I", "i", "J", "j", "K", "k", "L", "l", "M", "m", "N", "n", "O", "o", "P", "p", "Q", "q",
            "R", "r", "S", "s", "T", "t", "U", "u", "V", "v", "W", "w", "X", "x", "Y", "y", "Z", "z"};

    public static final String ALPHA_STR = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";

    public static final int ALPHA_STR_LEN = ALPHA_STR.length();

    public static final String ALPHA_NUM_STR = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";
    public static final int ALPHA_NUM_STR_LEN = ALPHA_NUM_STR.length();

    public static final String ASCII_STR = "!\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
    public static final int ASCII_STR_LEN = ASCII_STR.length();
    public static final int ASCII_LEN = 127 - 32;

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


    public static String randomAlphaStrL(int len) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(ALPHA_STR.charAt(random.nextInt(ALPHA_STR_LEN)));
        }
        return builder.toString();
    }

    /**
     * 性能高 重复率低
     * 虽然远不及UUID的唯一性，但是性能很高，适用于瞬时唯一性要求不高的场景，例如traceId生成
     */
    public static String randomAlphaCharL(int len) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        char[] tmp = new char[len];
        for (int i = 0; i < len; i++) {
            tmp[i] = ALPHA_STR.charAt(random.nextInt(ALPHA_STR_LEN));
        }
        return new String(tmp);
    }

    public static String randomAlphaCharCalL(int len) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        char[] tmp = new char[len];
        for (int i = 0; i < len; i++) {
            int delta = random.nextInt(ALPHA_STR_LEN);
            if (delta < 26) {
                tmp[i] = (char) (65 + delta);
            } else {
                tmp[i] = (char) (97 + delta - 26);
            }
        }
        return new String(tmp);
    }


    public static String randomAsciiStrL(int len) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        char[] tmp = new char[len];
        for (int i = 0; i < len; i++) {
            tmp[i] = ASCII_STR.charAt(random.nextInt(ASCII_STR_LEN));
        }
        return new String(tmp);
    }

    /**
     * @see RandomStringUtils#randomAscii(int)
     */
    public static String randomAsciiL(int len) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        char[] tmp = new char[len];
        for (int i = 0; i < len; i++) {
            tmp[i] = (char) (random.nextInt(ASCII_LEN) + 32);
        }
        return new String(tmp);
    }

    public static String randomAlphaNumStrL(int len) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(ALPHA_NUM_STR.charAt(random.nextInt(ALPHA_NUM_STR_LEN)));
        }
        return builder.toString();
    }

    public static String randomAlphaAS(int len) {
        Random random = new SecureRandom();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(ALPHA[random.nextInt(LIST.size())]);
        }
        return builder.toString();
    }
}

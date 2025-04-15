package com.github.kuangcp.util;

import java.math.BigInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author kuangcp on 3/14/19-2:26 PM
 */
public class ShowBinary {

    public static String toBinary(Byte value) {
        return Integer.toBinaryString(value);
    }

    public static String toBinary(Integer value) {
        return Integer.toBinaryString(value);
    }

    public static String toBinary(Double value) {
        return Long.toBinaryString(Double.doubleToRawLongBits(value));
    }

    public static String toBinary(Float value) {
        return Float.toHexString(value);
    }

    /**
     * https://stackoverflow.com/questions/9655181/java-convert-a-byte-array-to-a-hex-string
     * 风险：当转整数后前缀出现0，会丢失字节
     */
    public static String byteToHex2(byte[] value) {
        return new BigInteger(1, value).toString(16);
    }

    public static String byteToHex(byte[] value) {
        return IntStream.range(0, value.length)
                .mapToObj(i -> String.format("%02X", value[i]))
                .collect(Collectors.joining());
    }

    public static byte[] hexToByte(String hex) {
        byte[] ans = new byte[hex.length() / 2];
        for (int i = 0; i < ans.length; i++) {
            int index = i * 2;
            int val = Integer.parseInt(hex.substring(index, index + 2), 16);
            ans[i] = (byte) val;
        }
//        for (byte b : ans) {
//            System.out.print(b + " ");
//        }
//        System.out.println(hex.length() + " " + ans.length);
        return ans;
    }
}

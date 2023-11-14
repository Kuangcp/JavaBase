package com.github.kuangcp.string;

import org.apache.commons.codec.binary.StringUtils;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author <a href="https://github.com/kuangcp">Github</a>
 * 2023-10-26 13:45
 */
public class NumberMaxDiffIndex {

    public static int findDiffIndexPoint(String a, String b) {
        if (StringUtils.equals(a, b)) {
            return 0;
        }
        if (Objects.isNull(a)) {
            a = "0.0";
        }
        if (Objects.isNull(b)) {
            b = "0.0";
        }

        int ap = a.indexOf(".");
        if (ap == -1) {
            ap = a.length();
            a += ".0";
        }
        int bp = b.indexOf(".");
        if (bp == -1) {
            bp = b.length();
            b += ".0";
        }
        if (ap != bp) {
            return Math.max(ap, bp);
        }

        int maxDiff = 0;
        for (int i = ap - 1; i >= 0; i--) {
            if (a.charAt(i) != b.charAt(i)) {
                maxDiff = ap - i;
            }
        }
        if (maxDiff != 0) {
            return maxDiff;
        }
        int al = a.length();
        int bl = b.length();
        int maxL = Math.max(al, bl);
        for (int i = ap + 1; i < maxL; i++) {
            char ac;
            if (i >= al) {
                ac = '0';
            } else {
                ac = a.charAt(i);
            }

            char bc;
            if (i >= bl) {
                bc = '0';
            } else {
                bc = b.charAt(i);
            }
            if (ac != bc) {
                return ap - i;
            }
        }

        return 0;
    }

    private static final Pattern pattern = Pattern.compile("\\.");
    public static int findDiffIndex(String a, String b) {
        if (StringUtils.equals(a, b)) {
            return 0;
        }
        if (Objects.isNull(a)) {
            a = "";
        }
        if (Objects.isNull(b)) {
            b = "";
        }

//        String[] aPair = a.split("\\.");
//        String[] bPair = b.split("\\.");

        String[] aPair = pattern.split(a, 0);
        String[] bPair = pattern.split(b, 0);
        String an = aPair[0];
        String bn = bPair[0];

        int length = an.length();
        if (length != bn.length()) {
            return Math.max(length, bn.length());
        }
        for (int i = 0; i < length; i++) {
            if (an.charAt(i) != bn.charAt(i)) {
                return length - i;
            }
        }
        String as;
        if (aPair.length < 2) {
            as = "0";
        } else {
            as = aPair[1];
        }
        String bs;
        if (bPair.length < 2) {
            bs = "0";
        } else {
            bs = bPair[1];
        }

        if (as.length() != bs.length()) {
            int d = Math.abs(as.length() - bs.length());
            if (as.length() < bs.length()) {
                as += repeat("0", d);
            } else {
                bs += repeat("0", d);
            }
        }

        int min = Math.min(as.length(), bs.length());
        for (int i = 0; i < min; i++) {
            if (as.charAt(i) != bs.charAt(i)) {
                return -(i + 1);
            }
        }

        return 0;
    }

    private static String repeat(CharSequence c, int count) {
        StringBuilder rs = new StringBuilder();
        for (int i = 0; i < count; i++) {
            rs.append(c);
        }
        return rs.toString();
    }
}

package com.lin.util;
import sun.misc.*;

public class CoderUtil {

    // encode s using utf-8
    public static String toUTF8(String s) {
        try {
            byte[] buf = s.getBytes();
            s = new String(buf, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    // encode s using BASE64
    public static String encode(String s) {
        return (new BASE64Encoder()).encode(s.getBytes());
    }

    // decode s using BASE64
    public static String decode(String s) {
        try {
            return new String((new BASE64Decoder()).decodeBuffer(s));
        } catch (Exception e) {
            return null;
        }
    }
}

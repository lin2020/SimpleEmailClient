package util;
import sun.misc.*;

public class MyBase64 {
    // encode s using BASE64
    public static String encode(String s) {
        if (s == null)  return null;
        return (new BASE64Encoder()).encode(s.getBytes());
    }
    // decode s using BASE64
    public static String decode(String s) {
        if (s == null)  return null;
        try {
            byte[] b = (new BASE64Decoder()).decodeBuffer(s);
            return new String(b);
        } catch (Exception e) {
            return null;
        }
    }
}

package test.lin.util;

import com.lin.util.*;

public class CoderUtilTest {
    public static void main(String[] args) {
        String s = "我是林嘉栋";
        String s2 = "ztLKx8HWvM62sA==";
        LogUtil.i(s);
        LogUtil.i(CoderUtil.encode(s));
        LogUtil.i(CoderUtil.decode(s2));
    }
}

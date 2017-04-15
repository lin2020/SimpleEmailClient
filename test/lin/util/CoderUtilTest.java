package test.lin.util;

import com.lin.util.*;

public class CoderUtilTest {
    public static void main(String[] args) {
        String s = "我是林嘉栋";
        LogUtil.i(CoderUtil.decode(CoderUtil.encode(s)));
        LogUtil.i(CoderUtil.encode(s));
    }
}

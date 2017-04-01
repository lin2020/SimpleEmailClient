package com.lin.util;

public class LogUtil {
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int LEVEL = VERBOSE;

    public static void v(String msg) {
        if (LEVEL <= VERBOSE) {
            System.out.println(msg);
        }
    }

    public static void d(String msg) {
        if (LEVEL <= DEBUG) {
            System.out.println(msg);
        }
    }

    public static void i(String msg) {
        if (LEVEL <= INFO) {
            System.out.println(msg);
        }
    }

    public static void w(String msg) {
        if (LEVEL <= WARN) {
            System.out.println(msg);
        }
    }

    public static void e(String msg) {
        if (LEVEL <= ERROR) {
            System.out.println(msg);
        }
    }
}

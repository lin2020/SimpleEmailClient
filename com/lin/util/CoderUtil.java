package com.lin.util;

import java.lang.String;
import sun.misc.*;

public class CoderUtil {

    // change html to txt
    public static String htmltoTxt(String html) {
        // 去掉换行
        html = html.replaceAll("\r\n|\n|\r", "");
        // 去掉注释
        html = html.replaceAll("<!--[\\s\\S]*?-->", "");
        // 去掉html
        html = html.replaceAll("<html[^>]*>|</html>", "");
        // 去掉head
        html = html.replaceAll("<head>[\\s\\S]*?</head>", "");
        // 去掉样式
        html = html.replaceAll("<style[^>]*>[\\s\\S]*?</style>", "");
        // 去掉js
        html = html.replaceAll("<script[^>]*>[\\s\\S]*?</script>", "");
        // 去掉word标签
        html = html.replaceAll("<w:[^>]+>[\\s\\S]*?</w:[^>]+>", "");
        // 去掉xml
        html = html.replaceAll("<xml>[\\s\\S]*?</xml>", "");
        // 去掉body
        html = html.replaceAll("<body[^>]*>|</body>", "");
        // 去掉其他标签
        html = html.replaceAll("<[^>]+>", "");
        html = html.replaceAll("</[^>]+>", "");
        html = html.replaceAll("&nbsp;", "");
        LogUtil.i(html);
        return html.trim();
    }

    // change s to utf-8
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

package test.lin.util;

import java.util.*;
import java.lang.String;
import java.text.SimpleDateFormat;

import com.lin.util.*;
import com.lin.model.*;
import com.lin.database.*;

public class SmtpUtilTest {
    public static void main(String[] args) throws Exception {
        // 构建测试邮箱
        User u = new User(1, "linjd", "abc_2020@sohu.com", "abc2020");

        // 构造测试邮件
        SimpleDateFormat df = new SimpleDateFormat("EE, M MMM yyyy hh:mm:ss Z", Locale.US);
        String date = df.format(new Date());
        Email e = new Email("uidl", 1, date, "发件箱", u.getEmail_addr(), "abc_2020@sohu.com;1780615543@qq.com;", ";", ";", "你好", "你好:\n  这是我的第一封邮件");

        String html = "<html><head><meta http-equiv=3D\"content-type\" content=3D\"text/html; charse=\n" +
                        "t=3DGB2312\"><style>body { line-height: 1.5; }body { font-size: 10.5pt; fon=\n" +
                        "t-family: 'Microsoft YaHei UI'; color: rgb(0, 0, 0); line-height: 1.5; }</=\n" +
                        "style></head><body>=0A<div><span></span><br></div>=0A<div>=C4=E3=BA=C3</di=\n" +
                        "v>=0A</body></html>";

        // 连接数据库
        Class.forName("org.sqlite.JDBC");

        // 获取实例
        EmailClientDB emailClientDB = EmailClientDB.getInstance();
        emailClientDB.insertUser(u);

        // 发送邮件
        SmtpUtil.sendEmail(e, html, new SmtpCallbackListener() {

            public void onConnect() {
                LogUtil.i("on connect");
            }

            public void onCheck() {
                LogUtil.i("on check");
            }

            public void onSend(long send_email_size, long total_email_size) {
                // LogUtil.i("on send");
                // LogUtil.i("send_email_size: " + send_email_size + " total_email_size: " + total_email_size);
            }

            public void onFinish() {
                LogUtil.i("on finish");
            }

            public void onError() {
                LogUtil.i("on error");
            }
        });
    }
}

package test.lin.util;

import java.util.*;

import com.lin.util.*;
import com.lin.model.*;
import com.lin.database.*;

public class PopUtilTest {
    public static void main(String[] args) throws Exception {
        String server = "pop.sohu.com";
        int port = 110;
        String user = "abc_2020@sohu.com";
        String pass = "abc2020";
        User u = new User(1, "linjd", "abc_2020@sohu.com", "abc2020");

        // load driver
        Class.forName("org.sqlite.JDBC");
        // get instance
        EmailClientDB emailClientDB = EmailClientDB.getInstance();

        PopUtil.authentication(server, port, user, pass);

        PopUtil.retrEmails(u, new PopCallbackListener() {
            // 连接服务器
            @Override
            public void onConnect() {
                LogUtil.i("on connect");
            }
            // 检查邮件列表
            @Override
            public void onCheck() {
                LogUtil.i("on check");
            }
            // 下载邮件
            @Override
            public void onDownLoad(long total_email_size, long download_email_size,
                                   int total_email_count, int download_email_count,
                                   long current_email_size, long current_download_size) {
                LogUtil.i("total_size = " + total_email_size + " download_email_size = " + download_email_size);
                LogUtil.i("total_email_count = " + total_email_count + " download_email_count = " + download_email_count);
                LogUtil.i("current_email_size = " + current_email_size + " current_download_size = " + current_download_size);
            }
            // 下载完成
            @Override
            public void onFinish() {
                LogUtil.i("on finish");
            }
            // 下载出错
            @Override
            public void onError() {
                LogUtil.i("on error");
            }
            // 取消下载
            @Override
            public boolean onCancel() {
                LogUtil.i("on cancel");
                return true;
            }
        });

        List<Email> emails = emailClientDB.loadEmails(1, "收件箱");
        for (Email e : emails) {
            LogUtil.i(e.toString());
        }
        for (Email e : emails) {
            if (e.getSubject().equals("")) {
                PopUtil.sendDeleRequest(u, e);
                emailClientDB.deleteEmail(e);
            }
        }
    }
}

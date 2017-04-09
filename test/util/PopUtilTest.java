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

        PopUtil.retrEmails(u);

        List<Email> emails = emailClientDB.loadEmails(1, "收件箱");
        for (Email e : emails) {
            LogUtil.i(e.toString());
        }
    }
}

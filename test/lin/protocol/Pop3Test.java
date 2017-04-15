package test.lin.protocol;

import java.util.*;

import com.lin.model.*;
import com.lin.protocol.*;
import com.lin.util.*;

public class Pop3Test {

    public static void main(String[] args) {
        // sohu mail user
        User user = new User(0, "lin", "abc_2020@sohu.com", "abc2020");

        // test pop3 function
        Pop3 pop = new Pop3("pop.sohu.com", user.getEmail_addr(), user.getEmail_pass());
        pop.list();
        Vector<String> response = pop.retr(1);
        // pop.dele(pop.size());
        pop.quit();
        
        // test email info
        Email email = new Email(user, response);
        LogUtil.i("---------------begin");
        LogUtil.i("From: " + email.getFrom());
        LogUtil.i("To_List: ");
        for (String s : email.getTo_list()) {
            LogUtil.i(s);
        }
        LogUtil.i("Subject: " + email.getTheme());
        LogUtil.i("Content:");
        LogUtil.i(email.getContent());
        LogUtil.i("---------------end");
    }

}

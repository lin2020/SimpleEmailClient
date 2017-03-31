package test.lin.protocol;

import java.util.*;

import com.lin.bean.*;
import com.lin.protocol.*;
import com.lin.util.*;

public class SmtpTest {

    public static void main(String[] args) {
        // 163 mail pop3/smtp server
        // User user = new User(0, "lin", "15172323141@163.com", "lin2020");
        User user = new User(0, "lin", "abc_2020@sohu.com", "abc2020");
        Vector<String> to_list = new Vector<String>();
        to_list.addElement(user.getEmail_addr());
        Email email = new Email("hello", user.getEmail_addr(), to_list, "hello\r\n.");

        // test smtp function
        Smtp smtp = new Smtp("smtp.sohu.com", user.getEmail_addr(), user.getEmail_pass());
        smtp.sendEmail(email);
        smtp.quit();
    }

}

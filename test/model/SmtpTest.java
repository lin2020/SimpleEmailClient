package test.lin.model;

import java.util.*;

import com.lin.bean.*;
import com.lin.model.*;
import com.lin.util.*;

public class SmtpTest {

    public static void main(String[] args) {
        User user = new User(0, "lin", "15172323141@163.com", "lin2020");
        Vector<String> to_list = new Vector<String>();
        to_list.addElement(user.getEmail_addr());
        Email email = new Email("hello", user.getEmail_addr(), to_list, "hello\r\n.");

        // test smtp function
        Smtp smtp = new Smtp("smtp.163.com", user.getEmail_addr(), user.getEmail_pass());
        smtp.sendEmail(email);
        smtp.quit();
    }

}

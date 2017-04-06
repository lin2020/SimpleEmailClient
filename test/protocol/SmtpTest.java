package test.lin.protocol;

import java.util.*;

import com.lin.model.*;
import com.lin.protocol.*;
import com.lin.util.*;

public class SmtpTest {

    public static void main(String[] args) {
        // sohu mail user
        User user = new User(0, "lin", "abc_2020@sohu.com", "abc2020");

        // email to be send
        Vector<String> to_list = new Vector<String>();
        to_list.addElement(user.getEmail_addr());
        to_list.addElement("1780615543@qq.com");
        to_list.addElement("523067817@qq.com");
        Email email = new Email("hello", user.getEmail_addr(), to_list, "helo, i am linjiadong.");

        // test smtp function
        Smtp smtp = new Smtp("smtp.sohu.com", user.getEmail_addr(), user.getEmail_pass());
        smtp.sendEmail(email);
        smtp.quit();
    }

}

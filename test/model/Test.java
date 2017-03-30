package test.lin.model;

import java.util.*;

import com.lin.bean.*;
import com.lin.model.*;
import com.lin.util.*;

public class Test {

    public static void main(String[] args) {
        User user = new User(0, "lin", "15172323141@163.com", "lin2020");
        Pop3 pop = new Pop3("pop.163.com", user.getEmail_addr(), user.getEmail_pass());
        Smtp smtp = new Smtp("smtp.163.com", user.getEmail_addr(), user.getEmail_pass());
    }

}

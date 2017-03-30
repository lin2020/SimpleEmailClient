package view;

import java.util.*;
import bean.*;
import model.*;
import util.*;

public class Login {

    public static void main(String[] args) {
        User user = new User(0, "lin", "15172323141@163.com", "lin2020");
        Pop3 pop = new Pop3("pop.163.com", user.getEmail_addr(), user.getEmail_pass());
        Smtp smtp = new Smtp("smtp.163.com", user.getEmail_addr(), user.getEmail_pass());
    }

}

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
        pop.retr(pop.size());
        pop.dele(pop.size());
        pop.quit();
    }

}

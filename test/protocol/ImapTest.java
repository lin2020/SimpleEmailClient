package test.lin.protocol;

import java.util.*;

import com.lin.model.*;
import com.lin.protocol.*;
import com.lin.util.*;

public class ImapTest {

    public static void main(String[] args) {
        // sohu mail user
        User user = new User(0, "lin", "abc_2020@sohu.com", "abc2020");

        // test imap function
        Imap imap = new Imap("imap.sohu.com", user.getEmail_addr(), user.getEmail_pass());
        imap.logout();
    }

}

package test.lin.database;

import java.util.*;
import java.sql.*;

import com.lin.database.*;
import com.lin.util.*;
import com.lin.model.*;

public class EmailClientDBTest {

    public static void main(String[] args) throws Exception {
        // user
        List<User> users = new ArrayList<User>();
        User user1 = new User(1, "lin", "abc_2020@sohu.com", "abc2020");
        User user2 = new User(3, "jia", "1780615543@qq.com", "hhhe");
        User user3 = new User(5, "dong", "15172323141@163.com", "vdnja");

        // email
        List<Email> emails = new ArrayList<Email>();
        String to_string = "1780615543@qq.com;abc_2020@sohu.com;15172323141@163.com;";
        String cc_string = "1780615543@qq.com;abc_2020@sohu.com;";
        String bcc_string = "1780615543@qq.com;";
        Email email1 = new Email("1780615543@qq.com", to_string, cc_string, bcc_string, "hello", "This is email 1");
        email1.setUidl("170401.s");
        email1.setUserid(1);
        email1.setInbox("收件箱");
        email1.setDate("Sun, 9 Apr 2017 11:36:06 +0800");
        Email email2 = new Email();
        email2.setUidl("170401.ss");
        email2.setUserid(2);
        email2.setInbox("垃圾箱");
        email2.setSubject("hello");
        email2.setFrom("abc_2020@sohu.com");
        email2.setContent("This is email 2");
        email2.setDate("Sun, 9 Apr 2017 11:36:06 +0800");
        Email email3 = new Email();
        email3.setUidl("170401.sss");
        email3.setUserid(1);
        email3.setInbox("收件箱");
        email3.setSubject("hello");
        email3.setFrom("15172323141@163.com");
        email3.setContent("This is email 3");
        email3.setDate("Sun, 9 Apr 2017 11:36:06 +0800");

        // load driver
        Class.forName("org.sqlite.JDBC");

        // create database
        EmailClientDB emailClientDB = EmailClientDB.getInstance();

        // * test user
        // insert
        emailClientDB.insertUser(user1);
        emailClientDB.insertUser(user2);
        emailClientDB.insertUser(user3);
        users = emailClientDB.loadUsers();
        for (User u : users) {
            LogUtil.i("id = " + u.getId() + " name = " + u.getName() + " addr = " + u.getEmail_addr() + " pass = " + u.getEmail_pass());
        }
        // delete
        emailClientDB.deleteUser(user2);
        emailClientDB.deleteUser(user3);
        users = emailClientDB.loadUsers();
        for (User u : users) {
            LogUtil.i("id = " + u.getId() + " name = " + u.getName() + " addr = " + u.getEmail_addr() + " pass = " + u.getEmail_pass());
        }
        // update
        user1.setName("linjd");
        emailClientDB.updateUser(user1);
        users = emailClientDB.loadUsers();
        for (User u : users) {
            LogUtil.i("id = " + u.getId() + " name = " + u.getName() + " addr = " + u.getEmail_addr() + " pass = " + u.getEmail_pass());
        }

        // * test email
        // insert
        emailClientDB.insertEmail(email1);
        emailClientDB.insertEmail(email2);
        emailClientDB.insertEmail(email3);
        LogUtil.i("loading where 1 and inbox -----------------------------------------------------");
        emails = emailClientDB.loadEmails(1, "收件箱");
        for (Email e : emails) {
            LogUtil.i("Uidl = " + e.getUidl() + " Userid = " + e.getUserid() + " Inbox = " + e.getInbox());
            LogUtil.i(e.toString());
        }
        LogUtil.i("loading where 2 and trash -----------------------------------------------------");
        emails = emailClientDB.loadEmails(2, "垃圾箱");
        for (Email e : emails) {
            LogUtil.i("Uidl = " + e.getUidl() + " Userid = " + e.getUserid() + " Inbox = " + e.getInbox());
            LogUtil.i(e.toString());
        }
        // delete
        emailClientDB.deleteEmail(email3);
        LogUtil.i("loading where 1 and inbox -----------------------------------------------------");
        emails = emailClientDB.loadEmails(1, "收件箱");
        for (Email e : emails) {
            LogUtil.i("Uidl = " + e.getUidl() + " Userid = " + e.getUserid() + " Inbox = " + e.getInbox());
            LogUtil.i(e.toString());
        }
    }
}

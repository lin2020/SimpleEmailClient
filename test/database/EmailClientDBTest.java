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
        User user1 = new User(1, "lin", "abc_2020", "abc2020");
        User user2 = new User(3, "jia", "12333", "hhhe");
        User user3 = new User(5, "dong", "2222", "vdnja");

        // email
        List<Email> emails = new ArrayList<Email>();
        Email email1 = new Email();
        email1.setUserid(1);
        email1.setInbox("inbox");
        email1.setTheme("hello");
        email1.setFrom("wangyi");
        email1.setContent("hello world");
        Email email2 = new Email();
        email2.setUserid(2);
        email2.setInbox("trash");
        email2.setTheme("hi");
        email2.setFrom("ali");
        email2.setContent("hi world");
        Email email3 = new Email();
        email3.setUserid(1);
        email3.setInbox("inbox");
        email3.setTheme("helo");
        email3.setFrom("tencent");
        email3.setContent("helo world");

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
        emailClientDB.deleteUser(user1);
        users = emailClientDB.loadUsers();
        for (User u : users) {
            LogUtil.i("id = " + u.getId() + " name = " + u.getName() + " addr = " + u.getEmail_addr() + " pass = " + u.getEmail_pass());
        }
        // update
        user2.setName("lin");
        emailClientDB.updateUser(user2);
        users = emailClientDB.loadUsers();
        for (User u : users) {
            LogUtil.i("id = " + u.getId() + " name = " + u.getName() + " addr = " + u.getEmail_addr() + " pass = " + u.getEmail_pass());
        }

        // * test email
        // insert
        emailClientDB.insertEmail(email1);
        emailClientDB.insertEmail(email2);
        emailClientDB.insertEmail(email3);
        emails = emailClientDB.loadEmails(1, "inbox");
        for (Email e : emails) {
            LogUtil.i("Userid = " + e.getUserid() + " Inbox = " + e.getInbox() + " Theme = " + e.getTheme());
        }
        emails = emailClientDB.loadEmails(2, "trash");
        for (Email e : emails) {
            LogUtil.i("Userid = " + e.getUserid() + " Inbox = " + e.getInbox() + " Theme = " + e.getTheme());
        }
        // delete
        emailClientDB.deleteEmail(email3);
        emails = emailClientDB.loadEmails(1, "inbox");
        for (Email e : emails) {
            LogUtil.i("Userid = " + e.getUserid() + " Inbox = " + e.getInbox() + " Theme = " + e.getTheme());
        }
    }
}

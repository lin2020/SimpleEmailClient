package com.lin.util;

import java.util.*;
import java.util.regex.*;
import java.util.*;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

import com.lin.util.*;
import com.lin.model.*;
import com.lin.database.*;

public class PopUtil {

    // 认证账号
    public static boolean authentication(String server, int port, String user, String pass) {
        try {
            // create pop socket
            Socket socket = new Socket(server, port);
            BufferedReader in_from_server = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out_to_server = new PrintWriter(socket.getOutputStream());
            String response = in_from_server.readLine();
            LogUtil.i("S: " + response);
            if (!("+OK".equals(response.substring(0,3)))) {
                return false;
            }
            // authentication
            out_to_server.println("USER " + user);
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "USER " + user);
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                return false;
            }
            out_to_server.println("PASS " + pass);
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "PASS " + pass);
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                return false;
            }
            // get status
            out_to_server.println("STAT");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "STAT");
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                return false;
            }
            // close connect
            out_to_server.println("QUIT");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "QUIT");
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 从服务器获取所有用户的所有邮件
    public static void retrAllEmails() {
        EmailClientDB emailClientDB = EmailClientDB.getInstance();
        List<User> users = emailClientDB.loadUsers();
        for (User u : users) {
            retrEmails(u);
        }
    }

    // 从服务器获取指定用户的所有邮件
    public static boolean retrEmails(User user) {
        EmailClientDB emailClientDB = EmailClientDB.getInstance();
        String[] addr_str = user.getEmail_addr().split("@");
        String server = "pop." + addr_str[1];
        int port = 110;
        try {
            // * create pop socket
            Socket socket = new Socket(server, port);
            BufferedReader in_from_server = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out_to_server = new PrintWriter(socket.getOutputStream());
            String response = in_from_server.readLine();
            LogUtil.i("S: " + response);
            if (!("+OK".equals(response.substring(0,3)))) {
                return false;
            }

            // * authentication
            out_to_server.println("USER " + user.getEmail_addr());
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "USER " + user.getEmail_addr());
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                return false;
            }
            out_to_server.println("PASS " + user.getEmail_pass());
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "PASS " + user.getEmail_pass());
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                return false;
            }

            // * get status
            out_to_server.println("STAT");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "STAT");
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                return false;
            }

            // * get list info
            out_to_server.println("LIST");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "LIST");
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                return false;
            }
            // ** read all list line
            HashMap<Integer, Integer> data_map = new HashMap<Integer, Integer>();
            boolean flag = true;
            while (flag) {
                response = in_from_server.readLine();
                LogUtil.i("S: " + response);
                if (".".equals(response)) {
                    flag = false;
                } else {
                    String[] num_str = response.split("\\s+");
                    data_map.put(Integer.parseInt(num_str[0]), Integer.parseInt(num_str[1]));
                }
            }

            // * get uidl info
            out_to_server.println("UIDL");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "UIDL");
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                return false;
            }
            // ** read all uidl line
            HashMap<Integer, String> uidl_map = new HashMap<Integer, String>();
            flag = true;
            while (flag) {
                response = in_from_server.readLine();
                LogUtil.i("S: " + response);
                if (".".equals(response)) {
                    flag = false;
                } else {
                    String[] num_str = response.split("\\s+");
                    uidl_map.put(Integer.parseInt(num_str[0]), num_str[1]);
                }
            }

            // * download emails
            // ** remove the emails that has been download
            int uidl_map_size = uidl_map.size();
            for (int i = 1; i <= uidl_map_size; i++) {
                String uidl = uidl_map.get(i);
                List<Email> rs = emailClientDB.queryEmails(uidl);
                if (!rs.isEmpty()) {
                    uidl_map.remove(i);
                    data_map.remove(i);
                }
            }
            // ** download the emails that has not been download
            for (Integer key : data_map.keySet()) {
                String uidl = uidl_map.get(key);
                out_to_server.println("RETR " + key);
                out_to_server.flush();
                response = in_from_server.readLine();
                LogUtil.i("C: RETR " + key);
                LogUtil.i("S: " + response);
                if (!"+OK".equals(response.substring(0,3))) {
                    return false;
                }
                // read email info
                Vector<String> lines = new Vector<String>();
                flag = true;
                while (flag) {
                    response = in_from_server.readLine();
                    LogUtil.i("S: " + response);
                    if (".".equals(response)) {
                        flag = false;
                    } else {
                        lines.addElement(response);
                    }
                }
                Email email = constructEmail(uidl, user.getId(), "收件箱", lines);
                emailClientDB.insertEmail(email);
            }

            // * close connect
            out_to_server.println("QUIT");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "QUIT");
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 根据服务器返回的结果构造邮件
    public static Email constructEmail(String uidl, Integer userid, String inbox, Vector<String> lines) {
        String date = "";
        String from = "";
        Vector<String> to_list = new Vector<String>();
        Vector<String> cc_list = new Vector<String>();
        Vector<String> bcc_list = new Vector<String>();
        String subject = "";
        String content = "";
        String email_regex = "(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)";
        String subject_regex = "[B][?].+?[?][=]";
        boolean is_content_text = false;
        boolean is_content_type = false;
        boolean get_content = false;
        for (String line : lines) {
            if (line.startsWith("Date:")) {
                date = line.substring(6);
            }
            else if (line.startsWith("From:")) {
                Pattern p = Pattern.compile(email_regex);
                Matcher m = p.matcher(line);
                if (m.find()) {
                    from = m.group();
                }
            } else if (line.startsWith("To:")) {
                Pattern p = Pattern.compile(email_regex);
                Matcher m = p.matcher(line);
                while (m.find()) {
                    to_list.addElement(m.group());
                }
            } else if(line.startsWith("Cc:")){
                Pattern p = Pattern.compile(email_regex);
                Matcher m = p.matcher(line);
                while (m.find()) {
                    cc_list.addElement(m.group());
                }
            } else if (line.startsWith("Subject:")) {
                Pattern p = Pattern.compile(subject_regex);
                Matcher m = p.matcher(line);
                if (m.find()) {
                    subject = CoderUtil.decode(m.group().substring(2, m.group().length() - 2));
                }
            } else if (line.startsWith("------")) {
                is_content_text = false;
                get_content = false;
            } else if (line.startsWith("Content-Type:")) {
                if (line.contains("text/plain")) {
                    is_content_type = true;
                } else {
                    is_content_type = false;
                }
            } else if (line.endsWith("base64")) {
                is_content_text = true;
            }

            if (is_content_text && is_content_type) {
                if (!get_content) {
                    get_content = true;
                } else {
                    content += CoderUtil.decode(line);
                }
            }
        }
        Email email = new Email(uidl, userid, date, inbox, from, to_list, cc_list, bcc_list, subject, content);
        LogUtil.i(email.toString());
        return email;
    }
}

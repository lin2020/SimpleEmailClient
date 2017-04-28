package com.lin.util;

import java.util.*;
import java.util.regex.*;
import java.util.*;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import java.io.*;

import com.lin.util.*;
import com.lin.model.*;
import com.lin.database.*;

public class PopUtil {

    // 认证账号
    public static boolean authentication(String server, int port, String user, String pass) {
        try {
            // 建立连接
            Socket socket = new Socket(server, port);
            BufferedReader in_from_server = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out_to_server = new PrintWriter(socket.getOutputStream());
            String response = in_from_server.readLine();
            LogUtil.i("S: " + response);
            if (!("+OK".equals(response.substring(0,3)))) {
                return false;
            }
            // 认证登陆
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
            // 获取状态
            out_to_server.println("STAT");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "STAT");
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                return false;
            }
            // 关闭连接
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

    // 从服务器获取指定用户的所有邮件
    // user 下载的用户邮箱
    // onlyDownloadTop 下载方式，true 表示只下载邮件头，false 表示下载整个邮件
    // listener 下载状态监听接口
    public static boolean retrEmails(User user, boolean onlyDownloadTop, PopCallbackListener listener) {
        EmailClientDB emailClientDB = EmailClientDB.getInstance();
        List<Email> emails = new ArrayList<Email>();
        String[] addr_str = user.getEmail_addr().split("@");
        String server = "pop." + addr_str[1];
        int port = 110;
        try {

            listener.onConnect();

            // 建立socket连接
            Socket socket = new Socket(server, port);
            BufferedReader in_from_server = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out_to_server = new PrintWriter(socket.getOutputStream());
            String response = in_from_server.readLine();
            LogUtil.i("S: " + response);
            if (!("+OK".equals(response.substring(0,3)))) {
                listener.onError();
                return false;
            }
            // 认证登陆
            out_to_server.println("USER " + user.getEmail_addr());
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "USER " + user.getEmail_addr());
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                listener.onError();
                return false;
            }
            out_to_server.println("PASS " + user.getEmail_pass());
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "PASS " + user.getEmail_pass());
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                listener.onError();
                return false;
            }
            // 获取邮箱状态
            out_to_server.println("STAT");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "STAT");
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                listener.onError();
                return false;
            }

            listener.onCheck();

            // 获取邮件的大小
            out_to_server.println("LIST");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "LIST");
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                listener.onError();
                return false;
            }
            // 从服务器返回的信息中读出邮件列表的大小
            HashMap<Integer, Long> data_map = new HashMap<Integer, Long>();
            boolean flag = true;
            while (flag) {
                response = in_from_server.readLine();
                LogUtil.i("S: " + response);
                if (".".equals(response)) {
                    flag = false;
                } else {
                    String[] num_str = response.split("\\s+");
                    data_map.put(Integer.parseInt(num_str[0]), Long.parseLong(num_str[1]));
                }
            }
            // 获取邮件的uidl
            out_to_server.println("UIDL");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "UIDL");
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                listener.onError();
                return false;
            }
            // 从服务器返回的信息中读出邮件列表的uidl
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
            // 检查邮件列表，将本地存在的邮件从列表中移除
            int uidl_map_size = uidl_map.size();
            for (int i = 1; i <= uidl_map_size; i++) {
                String uidl = uidl_map.get(i);
                List<Email> rs = emailClientDB.queryEmails(uidl);
                if (!rs.isEmpty()) {
                    uidl_map.remove(i);
                    data_map.remove(i);
                }
            }

            // 一共要下载的邮件的数量
            int total_email_count = uidl_map.size();
            // 已经下载的邮件的数量
            int download_email_count = 0;
            // 一共要下载的所有邮件的大小
            long total_email_size = 0;
            // 已经下载的所有邮件的大小
            long download_email_size = 0;
            // 当前邮件的大小
            long current_email_size = 0;
            // 当前邮件下载的大小
            long current_download_size = 0;
            for (Integer key : data_map.keySet()) {
                total_email_size += data_map.get(key);
            }

            listener.onDownLoad(total_email_size, download_email_size, total_email_count, download_email_count, current_email_size, current_download_size);

            // 从服务器下载本地没有的邮件
            for (Integer key : data_map.keySet()) {
                if (listener.onCancel() == true) {
                    return false;
                }
                String uidl = uidl_map.get(key);
                if (onlyDownloadTop) {
                    out_to_server.println("TOP " + key + " 1");
                    out_to_server.flush();
                    response = in_from_server.readLine();
                    LogUtil.i("C: TOP " + key + " 1");
                    LogUtil.i("S: " + response);
                    if (!"+OK".equals(response.substring(0,3))) {
                        listener.onError();
                        return false;
                    }
                } else {
                    out_to_server.println("RETR " + key);
                    out_to_server.flush();
                    response = in_from_server.readLine();
                    LogUtil.i("C: RETR " + key);
                    LogUtil.i("S: " + response);
                    if (!"+OK".equals(response.substring(0,3))) {
                        listener.onError();
                        return false;
                    }
                }
                current_email_size = data_map.get(key);
                current_download_size = 0;
                // 读取服务器返回的邮件信息
                Vector<String> lines = new Vector<String>();
                flag = true;
                while (flag) {
                    if (listener.onCancel() == true) {
                        return false;
                    }
                    response = in_from_server.readLine();
                    LogUtil.i("S: " + response);
                    if (".".equals(response)) {
                        flag = false;
                        download_email_count++;
                        listener.onDownLoad(total_email_size, download_email_size, total_email_count, download_email_count, current_email_size, current_download_size);
                    } else {
                        lines.addElement(response);
                        current_download_size += response.length() + 1;
                        download_email_size += response.length() + 1;
                        listener.onDownLoad(total_email_size, download_email_size, total_email_count, download_email_count, current_email_size, current_download_size);
                    }
                }
                Email email = constructEmail(uidl, user.getId(), "收件箱", lines);
                if (onlyDownloadTop) {
                    email.setContent("onlyDownloadTop");
                }
                emails.add(email);
            }

            // 关闭连接
            out_to_server.println("QUIT");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "QUIT");
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                listener.onError();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            listener.onError();
            return false;
        }
        // 将下载下来的邮件保存到数据库中
        if (!emails.isEmpty()) {
            emailClientDB.insertEmails(emails);
        }
        listener.onFinish();
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
        int attachment_num = 0;
        Vector<String> attachment_list = new Vector<String>();
        String attachments_path = "C:\\SimpleEmailClient\\attachments";
        File attachments_file = new File(attachments_path);
        String attachment_name = "";
        File attachment_file = null;
        FileOutputStream attachment_out = null;
        String email_regex = "(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)";
        String subject_regex = "[B][?].*?[?][=]";
        String filename_regex = "[B][?].*?[?][=]";
        String content_type = "";
        boolean is_content_text = false;
        boolean is_attachment_text = false;
        boolean get_content = false;
        boolean get_head = true;
        boolean has_plain_content = false;
        for (String line : lines) {

            // 读取头部信息
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
                } else {
                    subject = line.substring(9);
                }
            } else if (line.equals("X-Has-Attach: yes")) {
                attachment_num = -1;
            }

            // 读取每一部分的信息
            if (line.startsWith("------")) {
                is_content_text = false;
                is_attachment_text = false;
                get_content = false;
                get_head = false;
            }
            if (line.contains("Content-Type:")) {
                if (line.contains("text/plain")) {
                    content_type = "text/plain";
                    has_plain_content = true;
                } else if (line.contains("text/html")) {
                    content_type = "text/html";
                } else {
                    content_type = "attachment";
                }
                get_head = false;
            }
            if (line.endsWith("base64") || line.endsWith("quoted-printable")) {
                is_content_text = true;
            }
            if (line.contains("filename")) {
                if (attachment_num == -1) {
                    attachment_num = 0;
                }
                is_attachment_text = true;
                attachment_num++;
                Pattern p = Pattern.compile(filename_regex);
                Matcher m = p.matcher(line);
                if (m.find()) {
                    attachment_name = CoderUtil.decode(m.group().substring(2, m.group().length() - 2));
                } else {
                    Pattern pp = Pattern.compile("filename=\".+\"");
                    Matcher mm = pp.matcher(line);
                    if (mm.find()) {
                        attachment_name = mm.group().substring(10, mm.group().length() - 1);
                    } else {
                        attachment_name = line.substring(10);
                    }
                }
                attachment_list.addElement(attachment_name);
                try {
                    attachment_file = new File(attachments_file, attachment_name);
                    attachment_file.createNewFile();
                    attachment_out = new FileOutputStream(attachment_file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 读取邮件正文，类型为txt
            if (is_content_text &&  get_content && content_type.equals("text/plain")) {
                content += CoderUtil.decode(line);
            }

            // 读取邮件正文，类型为html
            if (is_content_text && get_content && content_type.equals("text/html") && !has_plain_content) {
                content += line;
            }

            // 读取邮件附件
            if (is_attachment_text && get_content && content_type.equals("attachment")) {
                try {
                    attachment_out.write(CoderUtil.decode_byte(line));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 判断是否到了正文
            if (line.equals("")) {
                get_content = true;
            }
        }
        if (!has_plain_content) {
            content = CoderUtil.htmltoTxt(content);
        }
        if (attachment_num != 0) {
            try {
                attachment_out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Email email = new Email(uidl, userid, date, inbox, from, to_list, cc_list, bcc_list, subject, content);
        email.setAttachment_num(attachment_num);
        email.setAttachment_list(attachment_list);
        LogUtil.i(email.toString());
        return email;
    }

    // 删除服务器上的邮件
    public static boolean sendDeleRequest(User user, Email email) {
        String[] addr_str = user.getEmail_addr().split("@");
        String server = "pop." + addr_str[1];
        int port = 110;
        String uidl = email.getUidl();
        try {
            // 建立连接
            Socket socket = new Socket(server, port);
            BufferedReader in_from_server = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out_to_server = new PrintWriter(socket.getOutputStream());
            String response = in_from_server.readLine();
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0, 3))) {
                return false;
            }
            // 认证登陆
            out_to_server.println("USER " + user.getEmail_addr());
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "USER " + user.getEmail_addr());
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0, 3))) {
                return false;
            }
            out_to_server.println("PASS " + user.getEmail_pass());
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "PASS " + user.getEmail_pass());
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0, 3))) {
                return false;
            }
            // 获取状态
            out_to_server.println("STAT");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "STAT");
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                return false;
            }
            // 获取uidl列表
            out_to_server.println("UIDL");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "UIDL");
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                return false;
            }
            // 从服务器返回的信息中读出邮件列表的uidl
            HashMap<Integer, String> uidl_map = new HashMap<Integer, String>();
            boolean flag = true;
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
            for(Integer key : uidl_map.keySet()) {
                if (uidl.equals(uidl_map.get(key))) {
                    // 删除对应的邮件
                    out_to_server.println("DELE " + key);
                    out_to_server.flush();
                    response = in_from_server.readLine();
                    LogUtil.i("C: " + "DELE " + key);
                    LogUtil.i("S: " + response);
                    if (!"+OK".equals(response.substring(0,3))) {
                        return false;
                    }
                    break;
                }
            }
            // 关闭连接
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
        }
        return true;
    }

    // 向服务器发起获取整个邮件的请求
    // user 下载的用户邮箱，email 要下载的邮件
    // listener 下载状态监听接口
    public static boolean sendRetrRequest(User user, Email email, PopCallbackListener listener) {
        EmailClientDB emailClientDB = EmailClientDB.getInstance();
        String[] addr_str = user.getEmail_addr().split("@");
        String server = "pop." + addr_str[1];
        int port = 110;
        String uidl = email.getUidl();
        try {

            listener.onConnect();

            // 建立连接
            Socket socket = new Socket(server, port);
            BufferedReader in_from_server = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out_to_server = new PrintWriter(socket.getOutputStream());
            String response = in_from_server.readLine();
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0, 3))) {
                listener.onError();
                return false;
            }

            listener.onCheck();

            // 认证登陆
            out_to_server.println("USER " + user.getEmail_addr());
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "USER " + user.getEmail_addr());
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0, 3))) {
                listener.onError();
                return false;
            }
            out_to_server.println("PASS " + user.getEmail_pass());
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "PASS " + user.getEmail_pass());
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0, 3))) {
                listener.onError();
                return false;
            }
            // 获取状态
            out_to_server.println("STAT");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "STAT");
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                listener.onError();
                return false;
            }
            // 获取uidl列表
            out_to_server.println("UIDL");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "UIDL");
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                listener.onError();
                return false;
            }
            // 从服务器返回的信息中读出邮件列表的uidl
            HashMap<Integer, String> uidl_map = new HashMap<Integer, String>();
            boolean flag = true;
            while (flag) {
                if (listener.onCancel() == true) {
                    return false;
                }
                response = in_from_server.readLine();
                LogUtil.i("S: " + response);
                if (".".equals(response)) {
                    flag = false;
                } else {
                    String[] num_str = response.split("\\s+");
                    uidl_map.put(Integer.parseInt(num_str[0]), num_str[1]);
                }
            }
            for(Integer key : uidl_map.keySet()) {
                if (uidl.equals(uidl_map.get(key))) {
                    // 下载对应的邮件

                    // 获取邮件大小
                    out_to_server.println("LIST " + key);
                    out_to_server.flush();
                    response = in_from_server.readLine();
                    LogUtil.i("C: LIST " + key);
                    LogUtil.i("S: " + response);
                    if (!"+OK".equals(response.substring(0, 3))) {
                        listener.onError();
                        return false;
                    }
                    String[] data_num = response.split("\\s+");
                    long email_size = Long.parseLong(data_num[2]);
                    long download_size = 0;

                    listener.onDownLoad(0, 0, 0, 0, email_size, download_size);

                    // 正式下载邮件
                    out_to_server.println("RETR " + key);
                    out_to_server.flush();
                    response = in_from_server.readLine();
                    LogUtil.i("C: RETR " + key);
                    LogUtil.i("S: " + response);
                    if (!"+OK".equals(response.substring(0, 3))) {
                        listener.onError();
                        return false;
                    }
                    Vector<String> lines = new Vector<String>();
                    flag = true;
                    while (flag) {
                        response = in_from_server.readLine();
                        LogUtil.i("S: " + response);
                        if (".".equals(response)) {
                            flag = false;
                        } else {
                            lines.addElement(response);
                            download_size += response.length() + 1;
                            listener.onDownLoad(0, 0, 0, 0, email_size, download_size - 1);
                        }
                    }
                    Email email_new = constructEmail(uidl, user.getId(), "收件箱", lines);
                    emailClientDB.deleteEmail(email);
                    emailClientDB.insertEmail(email_new);
                    email.setContent(email_new.getContent());
                    email.setAttachment_num(email_new.getAttachment_num());
                    email.setAttachment_list(email_new.getAttachment_list());
                    break;
                }
            }
            // 关闭连接
            out_to_server.println("QUIT");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "QUIT");
            LogUtil.i("S: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                listener.onError();
                return false;
            }
        } catch (Exception e) {
            listener.onError();
            e.printStackTrace();
        }
        listener.onFinish();
        return true;
    }
}

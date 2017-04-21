package com.lin.util;

import java.util.*;
import java.util.regex.*;
import java.util.*;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.lang.System;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.FileReader;
import java.io.*;

import com.lin.util.*;
import com.lin.model.*;
import com.lin.database.*;

public class SmtpUtil {

    public static boolean sendEmail(Email email, String kind, boolean hasAttach, List<File> attachFile, SmtpCallbackListener listener) {
        String server = "smtp." + email.getFrom().split("@")[1];
        int port = 25;
        EmailClientDB emailClientDB = EmailClientDB.getInstance();
        List<User> users = emailClientDB.loadUsers();
        User user = null;
        for (User u : users) {
            if (u.getEmail_addr().equals(email.getFrom())) {
                user = u;
            }
        }
        if (user == null) {
            listener.onError();
            return false;
        }
        try {

            listener.onConnect();

            // 建立连接
            Socket sock = new Socket(server, port);
            sock.setSoTimeout(10 * 1000);
            BufferedReader in_from_server = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            PrintWriter out_to_server = new PrintWriter(sock.getOutputStream());
            String response = in_from_server.readLine();
            LogUtil.i("S: " + response);
            if (!"220".equals(response.substring(0, 3))) {
                listener.onError();
                return false;
            }

            listener.onCheck();

            // 验证登录
            out_to_server.println("EHLO " + server);
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "EHLO " + server);
            LogUtil.i("S: " + response);
            if (!"250".equals(response.substring(0, 3))) {
                listener.onError();
                return false;
            }
            while (!"250 ".equals(response.substring(0, 4))) {
                response = in_from_server.readLine();
                LogUtil.i("S: " + response);
            }
            out_to_server.println("AUTH LOGIN");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "AUTH LOGIN");
            LogUtil.i("S: " + response);
            if (!"334".equals(response.substring(0, 3))) {
                listener.onError();
                return false;
            }
            out_to_server.println(CoderUtil.encode(user.getEmail_addr().split("@")[0]));
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + CoderUtil.encode(user.getEmail_addr().split("@")[0]));
            LogUtil.i("S: " + response);
            if (!"334".equals(response.substring(0, 3))) {
                listener.onError();
                return false;
            }
            out_to_server.println(CoderUtil.encode(user.getEmail_pass()));
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + CoderUtil.encode(user.getEmail_pass()));
            LogUtil.i("S: " + response);
            if (!"235".equals(response.substring(0, 3))) {
                listener.onError();
                return false;
            }

            // 输入邮件的发送者
            out_to_server.println("MAIL FROM: <" + email.getFrom() + ">");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "MAIL FROM: <" + email.getFrom() + ">");
            LogUtil.i("S: " + response);
            if (!"250".equals(response.substring(0, 3))) {
                listener.onError();
                return false;
            }

            // 输入邮件的接收者
            for (String to : email.getTo_list()) {
                out_to_server.println("RCPT TO: <" + to + ">");
                out_to_server.flush();
                response = in_from_server.readLine();
                LogUtil.i("C: " + "RCPT TO: <" + to + ">");
                LogUtil.i("S: " + response);
                if (!"250".equals(response.substring(0, 3))) {
                    listener.onError();
                    return false;
                }
            }
            for (String cc : email.getCc_list()) {
                out_to_server.println("RCPT TO: <" + cc + ">");
                out_to_server.flush();
                response = in_from_server.readLine();
                LogUtil.i("C: " + "RCPT TO: <" + cc + ">");
                LogUtil.i("S: " + response);
                if (!"250".equals(response.substring(0, 3))) {
                    listener.onError();
                    return false;
                }
            }
            for (String bcc : email.getBcc_list()) {
                out_to_server.println("RCPT TO: <" + bcc + ">");
                out_to_server.flush();
                response = in_from_server.readLine();
                LogUtil.i("C: " + "RCPT TO: <" + bcc + ">");
                LogUtil.i("S: " + response);
                if (!"250".equals(response.substring(0, 3))) {
                    listener.onError();
                    return false;
                }
            }
            out_to_server.println("DATA");
            out_to_server.flush();
            response = in_from_server.readLine();
            if (!"354".equals(response.substring(0, 3))) {
                listener.onError();
                return false;
            }

            // 构造邮件的报文
            Vector<String> lines = new Vector<String>();
            lines.addElement("Date: " + email.getDate());
            lines.addElement("From: <" + email.getFrom() + ">");
            for (String to : email.getTo_list()) {
                lines.addElement("To: <" + to + ">");
            }
            for (String cc : email.getCc_list()) {
                lines.addElement("Cc: <" + cc + ">");
            }
            lines.addElement("Subject: " + "=?GB2312?B?" + CoderUtil.encode(email.getSubject()) + "?=");
            lines.addElement("X-Priority: 3");
            if (hasAttach) {
                lines.addElement("X-Has-Attach: yes");
            } else {
                lines.addElement("X-Has-Attach: no");
            }
            lines.addElement("X-Mailer: SimpleEmailClient 1, 0, 0, 0[cn]");
            lines.addElement("Mime-Version: 1.0");
            lines.addElement("Message-ID: <" + System.currentTimeMillis() + email.getFrom().split("@")[1] + ">");
            lines.addElement("Content-Type: multipart/mixed;");
            lines.addElement("	boundary=\"----=_001_NextPart464060244226_=----\"");
            lines.addElement("");
            lines.addElement("This is a multi-part message in MIME format.");
            lines.addElement("");
            lines.addElement("------=_001_NextPart464060244226_=----");
            if (kind.equals("txt")) {
                lines.addElement("Content-Type: text/plain;");
                lines.addElement("	charset=\"GB2312\"");
                lines.addElement("Content-Transfer-Encoding: base64");
                lines.addElement("");
                lines.addElement(CoderUtil.encode(email.getContent()));
                lines.addElement("");
            } else if (kind.equals("html")) {
                lines.addElement("Content-Type: text/html;");
                lines.addElement("	charset=\"GB2312\"");
                lines.addElement("Content-Transfer-Encoding: quoted-printable");
                lines.addElement("");
                lines.addElement(email.getContent());
            }
            if (hasAttach) {
                for (File file : attachFile) {
                    lines.addElement("------=_001_NextPart464060244226_=----");
                    lines.addElement("Content-Type: application/octet-stream;");
                    lines.addElement("	name=\"=?GB2312?B?" + CoderUtil.encode(file.getName()) + "?=\"");
                    lines.addElement("Content-Transfer-Encoding: base64");
                    lines.addElement("Content-Disposition: attachment;");
                    lines.addElement("	filename=\"=?GB2312?B?" + CoderUtil.encode(file.getName()) + "?=\"");
                    lines.addElement("");
                    sendFile(file, lines);
                    // lines.addElement(CoderUtil.encode(readFile(file)));
                    lines.addElement("");
                }
            }
            lines.addElement("------=_001_NextPart464060244226_=------");
            lines.addElement("");
            lines.addElement(".");

            // 统计报文的大小
            long total_email_size = 0;
            long send_email_size = 0;
            for (String s : lines) {
                total_email_size += s.length();
            }

            listener.onSend(send_email_size, total_email_size);

            // 发送报文
            for (String s : lines) {
                out_to_server.println(s);
                out_to_server.flush();
                LogUtil.i(s);
                send_email_size += s.length();
                listener.onSend(send_email_size, total_email_size);
            }
            response = in_from_server.readLine();
            LogUtil.i("S: " + response);
            if (!"250".equals(response.substring(0, 3))) {
                listener.onError();
                return false;
            }

            // 关闭连接
            out_to_server.println("QUIT");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("C: " + "QUIT");
            LogUtil.i("S: " + response);
            if (!"221".equals(response.substring(0, 3))) {
                listener.onError();
                return false;
            }
        } catch (Exception e) {
            listener.onError();
            e.printStackTrace();
            return false;
        }
        listener.onFinish();
        return true;
    }

    // 发送文件
    private static void sendFile(File file, Vector<String> lines) {
        if (file == null) {
            return;
        }
        try {
            InputStream in = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int byteRead = 0;
            while ((byteRead = in.read(buffer)) != -1) {
                lines.addElement(CoderUtil.encode(buffer));
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 读取文件
    public static byte[] readFile(File file) {
        byte[] byteArray = new byte[(int)file.length()];
        if (file == null) {
            return byteArray;
        }
        try {
            InputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int b = 0;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            byteArray = out.toByteArray();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteArray;
    }
}

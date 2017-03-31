package com.lin.model;

import java.util.*;
import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.lin.bean.*;
import com.lin.util.*;

public class Smtp {
    private static final int SMTP_PORT = 25;
    private String smtp_server = "";
    private String user_addr = "";
    private String user_pass = "";
    private Socket socket;
    private BufferedReader in_from_server;
    private PrintWriter out_to_server;
    private String response;

	public Smtp(String smtp_server, String user_addr, String user_pass) {
		super();
		this.smtp_server = smtp_server;
		this.user_addr = user_addr;
		this.user_pass = user_pass;
        loginSmtpServer();
	}

    private void loginSmtpServer() {
        try {
            // create smtp socket
            socket = new Socket(smtp_server, SMTP_PORT);
            in_from_server = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out_to_server = new PrintWriter(socket.getOutputStream());
            // read response
            response = in_from_server.readLine();
            LogUtil.i("Server: " + response);
            if (!"220".equals(response.substring(0,3))) {
                LogUtil.e("Connect Failed!");
            }
            // connect and login
            sendAndCheck("HELO " + user_addr, "250");
            sendAndCheck("AUTH LOGIN", "334");
            sendAndCheck(CoderUtil.encode(user_addr), "334");
            sendAndCheck(CoderUtil.encode(user_pass), "235");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEmail(Email email) {
        try {
            for (String to : email.getTo_list()) {
                sendAndCheck("MAIL FROM: <" + user_addr + ">", "250");
                sendAndCheck("RCPT TO: <" + to + ">", "250");
                sendAndCheck("DATA", "354");
                sendAndCheck(email.getContent(), "250");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void quit() {
        sendAndCheck("QUIT", "221");
    }

    private void sendAndCheck(String cmd, String status) {
        try {
            out_to_server.println(cmd);
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("Client: " + cmd);
            LogUtil.i("Server: " + response);
            if (!status.equals(response.substring(0, 3))) {
                LogUtil.e("Failed! " + "Excepted" + status + "; Received" + response.substring(0, 3));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

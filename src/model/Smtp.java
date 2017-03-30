package com.lin.model;

import java.util.*;
import java.net.Socket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.lin.util.*;

public class Smtp {
    private static final int SMTP_PORT = 25;
    private String smtp_server = "";
    private String email_addr = "";
    private String email_pass = "";
    private Socket socket;
    private BufferedReader in_from_server;
    private PrintWriter out_to_server;
    private String response;

	public Smtp(String smtp_server, String email_addr, String email_pass) {
		super();
		this.smtp_server = smtp_server;
		this.email_addr = email_addr;
		this.email_pass = email_pass;
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
                LogUtil.i("Connect Failed!");
            }
            // connect and login
            sendAndCheck("HELO " + email_addr, "250");
            sendAndCheck("AUTH LOGIN", "334");
            sendAndCheck(CoderUtil.encode(email_addr), "334");
            sendAndCheck(CoderUtil.encode(email_pass), "235");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendEmail(String from, String to) {
        try {
            //sendAndCheck();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void quit() {
        sendAndCheck("QUIT", "221");
    }

    private void sendAndCheck(String cmd, String status) {
        try {
            out_to_server.print(cmd + "\r\n");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("Client: " + cmd);
            LogUtil.i("Server: " + response);
            if (!status.equals(response.substring(0,3))) {
                LogUtil.i("Connect Failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.lin.protocol;

import java.util.*;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;

import com.lin.util.*;

public class Imap {
    private static final int IMAP_PORT = 143;
    private String imap_server = "";
    private String user_addr = "";
    private String user_pass = "";
    private Socket socket;
    private BufferedReader in_from_server;
    private PrintWriter out_to_server;
    private String response;
    private Integer sequence;

	public Imap(String imap_server, String user_addr, String user_pass) {
		super();
		this.imap_server = imap_server;
		this.user_addr = user_addr;
		this.user_pass = user_pass;
        loginImapServer();
	}

    private void loginImapServer() {
        try {
            // create socket and connect
            socket = new Socket(imap_server, IMAP_PORT);
            in_from_server = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out_to_server = new PrintWriter(socket.getOutputStream());
            response = in_from_server.readLine();
            LogUtil.i("Server: " + response);
            if (!"* OK".equals(response.substring(0, 4))) {
                LogUtil.e("Connect Failed!");
            }
            // login
            sequence = 1;
            out_to_server.println("A" + sequence + " LOGIN " + user_addr + " " + user_pass);
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("Client: " + "A" + sequence + " LOGIN " + user_addr + " " + user_pass);
            LogUtil.i("Server: " + response);
            if (!("A" + sequence + " OK").equals(response.substring(0, 5))) {
                LogUtil.e("Login Failed");
            }
            sequence = (sequence + 1) % 10;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        try {
            out_to_server.println("A" + sequence + " LOGOUT");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("Client: " + "A" + sequence + " LOGOUT");
            LogUtil.i("Server: " + response);
            if (!("* BYE").equals(response.substring(0, 5))) {
                LogUtil.e("Logout Failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

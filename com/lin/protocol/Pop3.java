package com.lin.protocol;

import java.util.*;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

import com.lin.util.*;

public class Pop3 {
    private static final int POP_WITH_SSL = 995;
    private static final int POP_WITHOUT_SSL = 110;
    private static final int POP_PORT = POP_WITHOUT_SSL;
    private String pop_server = "";
    private String user_addr = "";
    private String user_pass = "";
    private Socket socket;
    private BufferedReader in_from_server;
    private PrintWriter out_to_server;
    private String response;
    private boolean status;

	public Pop3(String pop_server, String user_addr, String user_pass) {
		super();
		this.pop_server = pop_server;
		this.user_addr = user_addr;
		this.user_pass = user_pass;
        loginPopServer();
	}

    private void loginPopServer() {
        try {
            // create pop3 socket
            socket = new Socket(pop_server, POP_PORT);
            in_from_server = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out_to_server = new PrintWriter(socket.getOutputStream());
            response = in_from_server.readLine();
            LogUtil.i("Server: " + response);
            status = true;
            if (!("+OK".equals(response.substring(0,3)))) {
                LogUtil.e("Connect Failed!");
                status = false;
            }
            // connect and login
            sendAndCheck("USER " + user_addr);
            sendAndCheck("PASS " + user_pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vector<String> list() {
        Vector<String> lines = new Vector<String>();
        try {
            // send command "list"
            out_to_server.println("LIST");
            out_to_server.flush();
            LogUtil.i("Client: LIST");
            // read first line
            response = in_from_server.readLine();
            lines.addElement(response);
            LogUtil.i("Server: " + response);
            // status not +OK
            if (!("+OK".equals(response.substring(0,3)))) {
                LogUtil.e("List Failed!");
                return lines;
            }
            // read all lines
            boolean flag = true;
            while (flag) {
                response = in_from_server.readLine();
                lines.addElement(response);
                LogUtil.i("Server: " + response);
                if (".".equals(response)) {
                    flag = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }

    public Vector<String> retr(Integer num) {
        Vector<String> lines = new Vector<String>();
        try {
            // send command "retr"
            out_to_server.println("RETR " + num);
            out_to_server.flush();
            LogUtil.i("Client: RETR " + num);
            // read first line
            response = in_from_server.readLine();
            lines.addElement(response);
            LogUtil.i("Server: " + response);
            // status not OK
            if (!"+OK".equals(response.substring(0,3))) {
                LogUtil.e("Retr Failed!");
                return lines;
            }
            // read all lines
            boolean flag = true;
            while (flag) {
                response = in_from_server.readLine();
                lines.addElement(response);
                LogUtil.i("Server: " + response);
                if (".".equals(response)) {
                    flag = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lines;
    }

    public boolean dele(Integer num) {
        try {
            // send command "dele"
            out_to_server.println("DELE " + num);
            out_to_server.flush();
            LogUtil.i("Client: DELE "+ num);
            // read response
            response =  in_from_server.readLine();
            LogUtil.i("Server: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                LogUtil.e("Dele Failed!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void quit() {
        try {
            out_to_server.println("QUIT");
            out_to_server.flush();
            LogUtil.i("Client: QUIT");
            response = in_from_server.readLine();
            LogUtil.i("Server: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                LogUtil.e("Quit Failed!");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int size() {
        int size = 0;
        try {
            out_to_server.println("LIST");
            out_to_server.flush();
            response = in_from_server.readLine();
            if (!"+OK".equals(response.substring(0,3))) {
                size = -1;
            } else {
                boolean flag = true;
                while (flag) {
                    response = in_from_server.readLine();
                    if (!".".equals(response)) {
                        size++;
                    } else {
                        flag = false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    private void sendAndCheck(String cmd) {
        try {
            out_to_server.println(cmd);
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("Client: " + cmd);
            LogUtil.i("Server: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                status = false;
                LogUtil.e("Connect Failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getStatus() {
        return this.status;
    }

}

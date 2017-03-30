package model;

import java.util.*;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;

import util.*;

public class Pop3 {
    private static final int POP_PORT = 110;
    private String pop_server = "";
    private String user_addr = "";
    private String user_pass = "";
    private Socket socket;
    private BufferedReader in_from_server;
    private PrintWriter out_to_server;
    private String response;

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
            if (!("+OK".equals(response.substring(0,3)))) {
                LogUtil.i("Connect Failed!");
            }
            // connect and login
            sendAndCheck("USER " + user_addr);
            sendAndCheck("PASS " + user_pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendAndCheck(String cmd) {
        try {
            out_to_server.print(cmd + "\r\n");
            out_to_server.flush();
            response = in_from_server.readLine();
            LogUtil.i("Client: " + cmd);
            LogUtil.i("Server: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                LogUtil.i("Connect Failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vector<String> list() {
        Vector<String> lines = new Vector<String>();
        try {
            // send command "list"
            out_to_server.print("LIST" + "\r\n");
            out_to_server.flush();
            LogUtil.i("Client: LIST");
            // read first line
            response = in_from_server.readLine();
            lines.addElement(response);
            LogUtil.i("Server: " + response);
            // status not +OK
            if (!("+OK".equals(response.substring(0,3)))) {
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
            out_to_server.print("RETR " + num + "\r\n");
            out_to_server.flush();
            LogUtil.i("Client: RETR " + num);
            // read first line
            response = in_from_server.readLine();
            lines.addElement(response);
            LogUtil.i("Server: " + response);
            // status not OK
            if (!"+OK".equals(response.substring(0,3))) {
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
            out_to_server.print("DELE " + num + "\r\n");
            out_to_server.flush();
            LogUtil.i("Client: DELE "+ num);
            // read response
            response =  in_from_server.readLine();
            LogUtil.i("Server: " + response);
            if (!"+OK".equals(response.substring(0,3))) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void quit() {
        try {
            out_to_server.print("QUIT" + "\r\t");
            out_to_server.flush();
            LogUtil.i("Client: QUIT");
            response = in_from_server.readLine();
            LogUtil.i("Server: " + response);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int getMailCount() {
        Vector<String> lines = list();
        if (lines.size() == 0 || !"+OK".equals(lines.get(0).substring(0,3))) return -1;
        return lines.size() - 2;
    }
}

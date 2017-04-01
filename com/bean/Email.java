package com.lin.bean;

import java.util.*;

public class Email {
    private String theme = "";
    private String from = "";
    private Vector<String> to_list;
    private String content = "";

    // create email by server response
    public Email(Vector<String> lines) {
        super();
        //initEmail(lines);
    }

    // create email by client input
	public Email(String theme, String from, Vector<String> to_list, String content) {
		super();
		this.theme = theme;
		this.from = from;
		this.to_list = to_list;
		this.content = content;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Vector<String> getTo_list() {
		return to_list;
	}

	public void setTo_list(Vector<String> to_list) {
		this.to_list = to_list;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
        String to = "";
        for (String to_item : to_list) {
            to += to_item + ";";
        }
		return "Theme: " + theme + "\r\n" +
                "From: " + from + "\r\n" +
                "To_list: " + to + "\r\n" +
                "Content:" + content + "\r\n";
	}
}

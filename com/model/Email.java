package com.lin.model;

import java.util.*;

public class Email {
    private Integer id;
    private Integer userid;
    private String inbox = "";
    private String theme = "";
    private String from = "";
    private Vector<String> to_list;
    private String content = "";

    public Email() {
        super();
    }

	public Email(String theme, String from, Vector<String> to_list, String content) {
		super();
		this.theme = theme;
		this.from = from;
		this.to_list = to_list;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

	public String getInbox() {
		return inbox;
	}

	public void setInbox(String inbox) {
		this.inbox = inbox;
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
}

package com.lin.model;

import java.util.*;
import java.util.regex.*;

import com.lin.util.*;
import com.lin.model.*;

public class Email {
    private String uidl = "";
    private Integer userid;
    private String inbox = "";
    private String theme = "";
    private String from = "";
    private Vector<String> to_list;
    private String content = "";

    public Email(User user, Vector<String> response) {
        super();
        init(user, response);
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

    private void init(User user, Vector<String> response) {
        String email_regex = "(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)";
        String subject_regex = "[B][?].+?[?][=]";
        boolean is_content_text = false;
        boolean is_content_type = false;
        boolean get_content = false;
        for (String line : response) {
            if (line.startsWith("From:")) {
                Pattern p = Pattern.compile(email_regex);
                Matcher m = p.matcher(line);
                if (m.find()) {
                    LogUtil.i(m.group());
                    from = m.group();
                }
            } else if (line.startsWith("To:")) {
                Pattern p = Pattern.compile(email_regex);
                Matcher m = p.matcher(line);
                to_list = new Vector<String>();
                while (m.find()) {
                    LogUtil.i(m.group());
                    to_list.addElement(m.group());
                }
            } else if (line.startsWith("Subject:")) {
                Pattern p = Pattern.compile(subject_regex);
                Matcher m = p.matcher(line);
                if (m.find()) {
                    String subject = m.group().substring(2, m.group().length() - 2);
                    LogUtil.i(subject);
                    theme = CoderUtil.decode(subject);
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
    }

    public String getUidl() {
        return uidl;
    }

    public void setUidl(String uidl) {
        this.uidl = uidl;
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

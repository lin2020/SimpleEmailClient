package com.lin.model;

import java.util.*;
import java.util.regex.*;
import java.io.File;

import com.lin.util.*;
import com.lin.model.*;

public class Email {
    // 邮件唯一标示
    private String uidl = "";
    // 邮件的用户id
    private Integer userid;
    // 邮件的时间
    private String date = "";
    // 邮件所在的文件夹
    private String inbox = "";
    // 邮件的发送方
    private String from = "";
    // 邮件的发送对象列表
    private Vector<String> to_list = new Vector<String>();
    // 邮件的抄送对象列表
    private Vector<String> cc_list = new Vector<String>();
    // 邮件的密送对象列表
    private Vector<String> bcc_list = new Vector<String>();
    // 邮件的主题
    private String subject = "";
    // 邮件的内容
    private String content = "";
    // 邮件的附件数量
    private Integer attachment_num = 0;
    // 邮件的附件名称
    private Vector<String> attachment_list = new Vector<String>();

    // 构造函数

    public Email() {
        super();
    }

    public Email(User user, Vector<String> response) {
        super();
        init(user, response);
    }

    public Email(String from, String to_stirng, String cc_stirng, String bcc_stirng, String subject, String content) {
        super();
        this.from = from;
        setToByString(to_stirng);
        setCcByString(cc_stirng);
        setBccByString(bcc_stirng);
        this.subject = subject;
        this.content = content;
	}

	public Email(String from, Vector<String> to_list, Vector<String> cc_list, Vector<String> bcc_list, String subject, String content) {
        super();
		this.from = from;
		this.to_list = to_list;
        this.cc_list = cc_list;
        this.bcc_list = bcc_list;
        this.subject = subject;
		this.content = content;
	}

    public Email(String uidl, Integer userid, String date, String inbox, String from, String to_stirng, String cc_stirng, String bcc_stirng, String subject, String content) {
        super();
        this.uidl = uidl;
        this.userid = userid;
        this.date = date;
        this.inbox = inbox;
        this.from = from;
        setToByString(to_stirng);
        setCcByString(cc_stirng);
        setBccByString(bcc_stirng);
        this.subject = subject;
        this.content = content;
    }

	public Email(String uidl, Integer userid, String date, String inbox, String from, Vector<String> to_list, Vector<String> cc_list, Vector<String> bcc_list, String subject, String content) {
		super();
		this.uidl = uidl;
		this.userid = userid;
		this.date = date;
		this.inbox = inbox;
		this.from = from;
		this.to_list = to_list;
		this.cc_list = cc_list;
		this.bcc_list = bcc_list;
		this.subject = subject;
		this.content = content;
	}

    // 根据服务器返回的邮件内容，读出其中的信息

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
                while (m.find()) {
                    LogUtil.i(m.group());
                    to_list.addElement(m.group());
                }
            } else if(line.startsWith("Cc:")){
                Pattern p = Pattern.compile(email_regex);
                Matcher m = p.matcher(line);
                while (m.find()) {
                    LogUtil.i(m.group());
                    cc_list.addElement(m.group());
                }
            } else if (line.startsWith("Subject:")) {
                Pattern p = Pattern.compile(subject_regex);
                Matcher m = p.matcher(line);
                if (m.find()) {
                    String subject = m.group().substring(2, m.group().length() - 2);
                    LogUtil.i(subject);
                    subject = CoderUtil.decode(subject);
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

    // toString 和 fromString 方法
    // 用来将列表转换成字符串或者从字符串中恢复出列表

    public String getToString() {
        String to = "";
        for (String s : to_list) {
            to += s + ";";
        }
        return to;
    }

    public void setToByString(String to) {
        to_list.clear();
        String[] list = to.split(";");
        for (String s : list) {
            if (!s.equals("")) {
                to_list.addElement(s);
            }
        }
    }

    public String getCcString() {
        String cc = "";
        for (String s : cc_list) {
            cc += s + ";";
        }
        return cc;
    }

    public void setCcByString(String cc) {
        cc_list.clear();
        String[] list = cc.split(";");
        for (String s : list) {
            if (!s.equals("")) {
                cc_list.addElement(s);
            }
        }
    }

    public String getBccString() {
        String bcc = "";
        for (String s : bcc_list) {
            bcc += s + ";";
        }
        return bcc;
    }

    public void setBccByString(String bcc) {
        bcc_list.clear();
        String[] list = bcc.split(";");
        for (String s : list) {
            if (!s.equals("")) {
                bcc_list.addElement(s);
            }
        }
    }

    public void setAttachmentByString(String attachment) {
        attachment_list.clear();
        String[] list = attachment.split(";");
        for (String s : list) {
            if (!s.equals("")) {
                attachment_list.addElement(s);
            }
        }
    }

    public String getAttachmentString() {
        String attachment = "";
        for (String s : attachment_list) {
            attachment += s + ";";
        }
        return attachment;
    }

    // setter 和 getter 方法

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

	public String getInbox() {
		return inbox;
	}

	public void setInbox(String inbox) {
		this.inbox = inbox;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
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

    public Vector<String> getCc_list() {
		return cc_list;
	}

	public void setCc_list(Vector<String> cc_list) {
		this.cc_list = cc_list;
	}

    public Vector<String> getBcc_list() {
        return bcc_list;
    }

    public void setBcc_list(Vector<String> bcc_list) {
        this.bcc_list = bcc_list;
    }

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

    public Integer getAttachment_num() {
        return attachment_num;
    }

    public void setAttachment_num(Integer attachment_num) {
        this.attachment_num = attachment_num;
    }

    public Vector<String> getAttachment_list() {
        return attachment_list;
    }

    public void setAttachment_list(Vector<String> attachment_list) {
        this.attachment_list = attachment_list;
    }

    @Override
	public String toString() {
        String to = "";
        String cc = "";
        String bcc = "";
        to = getToString();
        cc = getCcString();
        bcc = getBccString();
        return  "Date: " + date + "\n" +
                "From: " + from + "\n" +
                "To_list: " + to + "\n" +
                "Cc_list: " + cc + "\n" +
                "Bcc_list: " + bcc + "\n" +
                "Subject: " + subject + "\n" +
                "Content:" + content + "\n";
	}
}

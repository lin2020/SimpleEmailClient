package bean;

import java.util.*;

public class Email {
    private String theme = "";
    private String from = "";
    private Vector<String> to_list;
    private String data = "";

    // create email by server response
    public Email(Vector<String> lines) {
        super();
        //initEmail(lines);
    }

    // create email by client input
	public Email(String theme, String from, Vector<String> to_list, String data) {
		super();
		this.theme = theme;
		this.from = from;
		this.to_list = to_list;
		this.data = data;
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

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
        String to = "";
        for (String to_item : to_list) {
            to = to + to_item + ";";
        }
		return "Theme: " + theme + "\r\n" +
                "From: " + from + "\r\n" +
                "To_list: " + to + "\r\n" +
                "Content:" + data + "\r\n";
	}
}

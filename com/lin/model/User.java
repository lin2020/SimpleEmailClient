package com.lin.model;
import java.util.*;

public class User {
    private Integer id;
    private String name = "";
    private String email_addr = "";
    private String email_pass = "";

    public User() {
        super();
    }

    public User(String email_addr, String email_pass) {
        super();
        this.email_addr = email_addr;
        this.email_pass = email_pass;
    }

	public User(Integer id, String name, String email_addr, String email_pass) {
		super();
		this.id = id;
		this.name = name;
		this.email_addr = email_addr;
		this.email_pass = email_pass;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail_addr() {
		return email_addr;
	}

	public void setEmail_addr(String email_addr) {
		this.email_addr = email_addr;
	}

	public String getEmail_pass() {
		return email_pass;
	}

	public void setEmail_pass(String email_pass) {
		this.email_pass = email_pass;
	}
}

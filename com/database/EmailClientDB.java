package com.lin.database;

import java.util.*;
import java.sql.*;
import java.io.*;

import com.lin.model.*;
import com.lin.util.*;

public class EmailClientDB {
	// database path
	private String dir_name = "C:\\SimpleEmailClient";
	// database name
	private String db_name = "jdbc:sqlite:" + dir_name + "\\email_client.db";
	// user table
	private static final String CREATE_USER = "create table if not exists user ("
												+ "id integer primary key autoincrement, "
												+ "name String, "
												+ "email_addr String, "
												+ "email_pass String)";
	// email table
	public static final String CREATE_EMAIL = "create table if not exists email ("
												+ "uidl String, "
												+ "userid integer, "
												+ "inbox String, "
												+ "from_addr String, "
												+ "to_list String, "
												+ "cc_list String, "
												+ "bcc_list String, "
												+ "theme String, "
												+ "content String)";

	// singleton
	private static EmailClientDB emailClientDB = null;

	// private construction
	private EmailClientDB() {
		onCreate();
	}

	// get instence
	public synchronized static EmailClientDB getInstance() {
		if (emailClientDB == null) {
			emailClientDB = new EmailClientDB();
		}
		return emailClientDB;
	}

	// if table user and email not exists, create
	private void onCreate() {
		// create file
		File dir = new File(dir_name);
		LogUtil.i("Create " + dir_name);
		if (dir.exists()) {
			LogUtil.w("File exists!");
		} else {
			if (!dir.mkdir()) {
				LogUtil.e("Create dir failed!");
			} else {
				LogUtil.i("Create dir success!");
			}
		}
		// create db
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(db_name);
			PreparedStatement pre;
			pre = connection.prepareStatement(CREATE_USER);
			pre.executeUpdate();
			pre = connection.prepareStatement(CREATE_EMAIL);
			pre.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// load all users
	public List<User> loadUsers() {
		List<User> list = new ArrayList<User>();
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(db_name);
			PreparedStatement pre = connection.prepareStatement("select * from user");
			ResultSet rs = pre.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setEmail_addr(rs.getString("email_addr"));
				user.setEmail_pass(rs.getString("email_pass"));
				list.add(user);
				LogUtil.i("Load user");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	// update a user
	public void updateUser(User user) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(db_name);
			int primaryKey = 0;
			PreparedStatement pre;
			pre = connection.prepareStatement("update user set name = ? where id = ?");
			pre.setString(1, user.getName());
			pre.setInt(2, user.getId());
			pre.executeUpdate();
			pre = connection.prepareStatement("update user set email_addr = ? where id = ?");
			pre.setString(1, user.getEmail_addr());
			pre.setInt(2, user.getId());
			pre.executeUpdate();
			pre = connection.prepareStatement("update user set email_pass = ? where id = ?");
			pre.setString(1, user.getEmail_pass());
			pre.setInt(2, user.getId());
			pre.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// insert a user
	public void insertUser(User user) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(db_name);
			PreparedStatement pre = connection.prepareStatement("insert into user values(null, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
			pre.setString(1, user.getName());
			pre.setString(2, user.getEmail_addr());
			pre.setString(3, user.getEmail_pass());
			pre.executeUpdate();
			user.setId(pre.getGeneratedKeys().getInt(1));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// delete a user
	public void deleteUser(User user) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(db_name);
			PreparedStatement pre = connection.prepareStatement("delete from user where id = ?");
			pre.setInt(1, user.getId());
			pre.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// load all emails
	public List<Email> loadEmails(Integer userid, String inbox) {
		List<Email> list = new ArrayList<Email>();
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(db_name);
			PreparedStatement pre = connection.prepareStatement("select * from email where userid = ? and inbox = ?");
			pre.setInt(1, userid);
			pre.setString(2, inbox);
			ResultSet rs = pre.executeQuery();
			while (rs.next()) {
				Email email = new Email();
				email.setUidl(rs.getString("uidl"));
				email.setUserid(rs.getInt("userid"));
				email.setInbox(rs.getString("inbox"));
				email.setFrom(rs.getString("from_addr"));
				email.setToByString(rs.getString("to_list"));
				email.setCcByString(rs.getString("cc_list"));
				email.setBccByString(rs.getString("bcc_list"));
				email.setTheme(rs.getString("theme"));
				email.setContent(rs.getString("content"));
				list.add(email);
				LogUtil.i("Load email");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	// insert an email
	public void insertEmail(Email email) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(db_name);
			PreparedStatement pre = connection.prepareStatement("insert into email values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pre.setString(1, email.getUidl());
			pre.setInt(2, email.getUserid());
			pre.setString(3, email.getInbox());
			pre.setString(4, email.getFrom());
			pre.setString(5, email.getToString());
			pre.setString(6, email.getCcString());
			pre.setString(7, email.getBccString());
			pre.setString(8, email.getTheme());
			pre.setString(9, email.getContent());
			pre.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// delete an email
	public void deleteEmail(Email email) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(db_name);
			PreparedStatement pre = connection.prepareStatement("delete from email where uidl = ?");
			pre.setString(1, email.getUidl());
			pre.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

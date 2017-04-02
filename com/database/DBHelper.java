package com.lin.database;

import java.util.*;
import java.sql.*;
import java.io.*;

import com.lin.bean.*;
import com.lin.util.*;

public class DBHelper {
	private static String dir_name = "C:\\SimpleEmailClient";
	private static String db_name = "jdbc:sqlite:" + dir_name + "\\data.db";

	public static void onCreate() {
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
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.executeUpdate("drop table if exists user");
			statement.executeUpdate("create table if not exists user (id integer, name String, email_addr String, email_pass String)");
			statement.executeUpdate("create table if not exists inbox (userid integer, theme String, from_addr String, to_addr String, head String, content String)");
			statement.executeUpdate("create table if not exists trash (userid integer, theme String, from_addr String, to_addr String, head String, content String )");
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

package com.lin.database;

import java.sql.*;
import java.io.*;

import com.lin.util.*;

public class DBHelper {
	private static String dir_name = "C:\\SimpleEmailClient";
	private static String db_name = "jdbc:sqlite:" + dir_name + "\\data.db";
	private static String tb_name = "person";

	public static void init() {
		initDir();
		initDB();
	}

	public static void update(String sm) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(db_name);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			statement.executeUpdate(sm);
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

	private static void initDir() {
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
	}

	private static void initDB() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(db_name);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			ResultSet rs = connection.getMetaData().getTables(null, null, tb_name, null);
			if (rs.next()) {
				LogUtil.i("Table exists");
			} else {
				statement.executeUpdate("create table " + tb_name + " (id integer, name String)");
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
	}

}

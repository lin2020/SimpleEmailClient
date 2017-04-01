package com.lin.database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBHelper {

	public DBHelper() {
		super();
        Class.forName(SQLITE_JDBC);
        Connection connection = null;
        try {
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
	}
}

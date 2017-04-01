package test.lin.database;

import java.sql.*;

import com.lin.database.*;

import com.lin.util.*;
public class DBHelperTest {

    private static String dir_name = "C:\\SimpleEmailClient";
    private static String db_name = "jdbc:sqlite:" + dir_name + "\\data.db";
    private static String tb_name = "person";

    public static void main(String[] args) throws Exception {
        // load driver
        Class.forName("org.sqlite.JDBC");

        // init
        DBHelper.init();

        // update
        DBHelper.update("insert into person values(1, 'jia')");
        DBHelper.update("delete from person where id = 1");

        // query
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(db_name);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery("select * from person");
            while (rs.next()) {
                LogUtil.i("id = " + rs.getInt("id") + "; name = " + rs.getString("name"));
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

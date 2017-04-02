package test.lin.database;

import java.sql.*;

import com.lin.database.*;

import com.lin.util.*;
import com.lin.bean.*;

public class DBHelperTest {

    private static String dir_name = "C:\\SimpleEmailClient";
    private static String db_name = "jdbc:sqlite:" + dir_name + "\\data.db";

    public static void main(String[] args) throws Exception {
        // user
        User user = new User(1, "lin", "abc_2020", "abc2020");

        // load driver
        Class.forName("org.sqlite.JDBC");

        // create
        DBHelper.onCreate();

        // operate
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(db_name);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            ResultSet rs;

            // * test insert
            LogUtil.i("* Test Insert");
            PreparedStatement pre = connection.prepareStatement("insert into user values(?, ?, ?, ?)");
            // insert 1
            pre.setInt(1, user.getId());
            pre.setString(2, user.getName());
            pre.setString(3, user.getEmail_addr());
            pre.setString(4, user.getEmail_pass());
            pre.executeUpdate();
            // insert 2
            pre.setInt(1, 2);
            pre.setString(2, "test");
            pre.setString(3, "lin2020");
            pre.setString(4, "lin2020");
            pre.executeUpdate();
            // query
            pre = connection.prepareStatement("select * from user");
            rs = pre.executeQuery();
            while (rs.next()) {
                LogUtil.i("id = " + rs.getInt("id"));
                LogUtil.i("name = " + rs.getString("name"));
                LogUtil.i("email_addr = " + rs.getString("email_addr"));
                LogUtil.i("email_pass = " + rs.getString("email_pass"));
            }

            // * test delete
            LogUtil.i("* Test Delete");
            pre = connection.prepareStatement("delete from user where id = ?");
            pre.setInt(1, 1);
            pre.executeUpdate();
            // query
            pre = connection.prepareStatement("select * from user");
            rs = pre.executeQuery();
            while (rs.next()) {
                LogUtil.i("id = " + rs.getInt("id"));
                LogUtil.i("name = " + rs.getString("name"));
                LogUtil.i("email_addr = " + rs.getString("email_addr"));
                LogUtil.i("email_pass = " + rs.getString("email_pass"));
            }

            // * test update
            LogUtil.i("* Test Update");
            pre = connection.prepareStatement("update user set name = ? where id = ?");
            pre.setString(1, "jia");
            pre.setInt(2, 2);
            pre.executeUpdate();
            pre = connection.prepareStatement("select * from user");
            rs = pre.executeQuery();
            while (rs.next()) {
                LogUtil.i("id = " + rs.getInt("id"));
                LogUtil.i("name = " + rs.getString("name"));
                LogUtil.i("email_addr = " + rs.getString("email_addr"));
                LogUtil.i("email_pass = " + rs.getString("email_pass"));
            }

            // * test query
            LogUtil.i("* Test Query");
            pre = connection.prepareStatement("select * from user where name = ?");
            pre.setString(1, "jia");
            rs = pre.executeQuery();
            while (rs.next()) {
                LogUtil.i("id = " + rs.getInt("id"));
                LogUtil.i("name = " + rs.getString("name"));
                LogUtil.i("email_addr = " + rs.getString("email_addr"));
                LogUtil.i("email_pass = " + rs.getString("email_pass"));
            }

            // // insert
            // statement.executeUpdate("insert into user values(0, ?, ?, ?)",
            //             new String[] {user.getName(), user.getEmail_addr(), user.getEmail_pass()});
            // statement.executeUpdate("insert into user valuse(1, ?, ?, ?)",
            //             new String[] {"test", "lin2020", "lin2020"});
            // rs = statement.executeQuery("select id from user where name = " + user.getName() +
            //             " and email_addr = " + user.getEmail_addr() + " and email_pass = " + user.getEmail_pass());
            // if (!rs.next()) {
            //     user.setId(rs.getInt("id"));
            //     LogUtil.i("insert success");
            //     LogUtil.i("user id = " + user.getId());
            // } else {
            //     LogUtil.e("insert failed");
            // }
            //
            // // delete
            // statement.executeUpdate("delete from user where id = " + user.getId());
            // rs = statement.executeQuery("select id from user where id = " + user.getId());
            // if (rs.next()) {
            //     LogUtil.i("delete success");
            // } else {
            //     LogUtil.e("delete failed");
            // }
            //
            // // update
            // statement.executeUpdate("update user set name = ? where email_addr = ? and email_pass = ?",
            //             new String[] {"jia", "lin2020", "lin2020"});
            // rs = statement.executeQuery("select name from user where email_addr = lin2020 and email_pass = lin2020");
            // if (rs.next()) {
            //     LogUtil.e("update failed");
            // } else {
            //     LogUtil.i("update success");
            //     LogUtil.i("user name = " + rs.getString("name"));
            // }
            //
            // // query
            // rs = statement.executeQuery("select * from user");
            // while (rs.next()) {
            //     LogUtil.i("id = " + rs.getInt("id"));
            //     LogUtil.i("name = " + rs.getString("name"));
            //     LogUtil.i("email_addr = " + rs.getString("email_addr"));
            //     LogUtil.i("email_pass = " + rs.getString("email_pass"));
            // }

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

package javaDemo.util;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public class JDBCUtil {

    public static String url = "jdbc:mysql://localhost:3306/javademo?"
            + "user=root&password=lxh960119&useUnicode=true&characterEncoding=UTF8";
    public static Connection conn = null;

    public static Statement stmt = null;

    public static void init(String sql) throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("成功加载MySQL驱动程序");
        conn = DriverManager.getConnection(url);
        stmt = conn.createStatement();
    }

    public static boolean createTable(String createTable , int columnNum){
        int result = -1;
        try {
            String sql = "create table" +createTable+"(";

            for (int i = 0 ; i < columnNum ; i ++){
                sql += "NO"+i+"varchar(20),";
            }
            sql = sql.substring(0,sql.length()-1);
            sql += ")";

            result = stmt.executeUpdate(sql);// executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result == -1 ? false : true;

    }

    public static boolean insert(String sql) throws SQLException {

        int result = -1;
        try {
                System.out.println("创建数据表成功");
                //sql = "insert into student(NO,name) values('2012001','陶伟基')";
                result = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }

        return result == -1 ? false : true;

    }
}

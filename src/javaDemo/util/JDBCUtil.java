package javaDemo.util;

import java.sql.*;
import java.util.ArrayList;

public class JDBCUtil {

    public static String url = "jdbc:mysql://localhost:3306/javaTest?"
            + "useUnicode=true&characterEncoding=UTF8";
    public static Connection conn = null;

    public static Statement stmt = null;

    public static void init(String url, String driverName, String userName, String password) throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("成功加载MySQL驱动程序");
        conn = DriverManager.getConnection(url,userName,password);
        stmt = conn.createStatement();
    }

    public static ArrayList<String> getResult(String sql){
        ArrayList<String> result = new ArrayList<String>();
        try {
            ResultSet tmp = stmt.executeQuery(sql);// executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功
            ResultSetMetaData rsmd = tmp.getMetaData() ;
            int columnCount = rsmd.getColumnCount();
            while (tmp.next()){
                String content = "";
                for(int i = 1 ; i <= columnCount; i ++){
                    content += (tmp.getString(i)+" ");
                }
                result.add(new String(content));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 2.关闭连接
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }

    public static boolean createTable(String createTable , int columnNum){
        int result=-1,result1 = -1;
        try {
            String tmp = "drop table if exists " +createTable+"";
            String sql = "create table " +createTable+" (";

            for (int i = 0 ; i < columnNum ; i ++){
                sql += "NO"+i+" varchar(50),";
            }
            sql = sql.substring(0,sql.length()-1);
            sql += ")";
            stmt.executeUpdate(tmp);
            result1 = stmt.executeUpdate(tmp);
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
                //sql = "insert into student values('2012001','陶伟基')";
                result = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result == -1 ? false : true;

    }
}

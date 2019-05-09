package demo;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbUtil {

    /*
     * 打开数据库
     */
    private static String driver;//连接数据库的驱动
    private static String url;
    private static String username;
    private static String password;
    private List<Connection> list = new ArrayList<Connection>();

    static {
        driver = "com.mysql.jdbc.Driver";//需要的数据库驱动
        url = "jdbc:mysql://localhost:3306/test1";//数据库名路径
        username = "root";
        password = "root";
    }

    private static Connection conn = null;

    public static Connection open() {
        try {
            if(conn!=null){
                return conn;
            }
            Class.forName(driver);
            return conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.out.println("数据库连接失败！");
            e.printStackTrace();
        }//加载驱动
        return null;
    }

    public static Connection getConn() {
        try {
            Class.forName(driver);
            return (Connection) DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.out.println("数据库连接失败！");
            e.printStackTrace();
        }//加载驱动
        return null;
    }

    /*
     * 关闭数据库
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static List convertList(ResultSet rs) throws SQLException{
        List list = new ArrayList();
        ResultSetMetaData md = rs.getMetaData();//获取键名
        int columnCount = md.getColumnCount();//获取行的数量
        while (rs.next()) {
            Map rowData = new HashMap();//声明Map
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));//获取键名及值
            }
            list.add(rowData);
        }
        return list;
    }

    public static List executeQuery(String sql) {
        Connection conn = DbUtil.open();
        PreparedStatement pstmt = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            List list = DbUtil.convertList(rs);
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            /*if(pstmt!=null){
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }*/
        }
        return null;
    }

    public static int executeUpdate(String sql){
        Connection conn = DbUtil.open();
        PreparedStatement pstmt = null;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            int result = pstmt.executeUpdate();
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            /*if(pstmt!=null){
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }*/
            if(conn!=null){
               /* try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }*/
            }
        }
        return 0;
    }


}

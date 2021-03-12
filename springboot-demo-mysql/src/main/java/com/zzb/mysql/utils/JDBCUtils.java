package com.zzb.mysql.utils;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;

/**
 * 类名称：JDBCUtils
 * 类描述：TODO
 * 创建人：赵增斌
 * 修改人：赵增斌
 * 修改时间：2020/9/23 10:35 上午
 * 修改备注：TODO
 */
public class JDBCUtils {
    private static String url;
    private static String user;
    private static String password;
    private static String driver;

    /**
     * 文件读取，只会执行一次，使用静态代码块
     */
    static {
        //读取文件，获取值
        try {
            //1.创建Properties集合类
            Properties pro = new Properties();
            //获取src路径下的文件--->ClassLoader类加载器
            ClassLoader classLoader = JDBCUtils.class.getClassLoader();
            URL resource = classLoader.getResource("jdbc.properties");
            ;
            String path = resource.getPath();
            //2.加载文件
            pro.load(new FileReader(path));
            //3获取数据
            url = pro.getProperty("url");
            user = pro.getProperty("user");
            password = pro.getProperty("password");
            driver = pro.getProperty("driver");
            //4.注册驱动
            Class.forName(driver);
            System.out.println(DriverManager.getLoginTimeout());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 方法：getConnection
     * 描述：获取连接
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @return : java.sql.Connection
     * @date: 2020年09月23日 10:36 上午
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    /**
     * 方法：close
     * 描述：释放资源
     * 作者：赵增斌 E-mail:zhaozengbin@gmail.com QQ:4415599 weibo:http://weibo.com/zhaozengbin
     *
     * @param rs   :
     * @param st   :
     * @param conn :
     * @return : void
     * @date: 2020年09月23日 10:36 上午
     */
    public static void close(ResultSet rs, Statement st, Connection conn) {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


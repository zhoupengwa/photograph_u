package com.photograph_u.util;

import com.photograph_u.consts.JdbcConsts;

import java.sql.*;

public class JdbcUtils {
    private static ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

    //c3p0连接池获取连接
    /*
    private static ComboPooledDataSource dataSource = new ComboPooledDataSource("mysql");

    public static Connection getConnection() throws SQLException {
        Connection conn = connectionThreadLocal.get();
        if (conn == null) {
            conn = dataSource.getConnection();
        }
        return conn;
    }
    */
    //JDBC获取链接
    public static Connection getConnection() throws SQLException {
        Connection conn = connectionThreadLocal.get();
        if (conn == null) {
            try {
                Class.forName(JdbcConsts.DRIVER);
                conn = DriverManager.getConnection(JdbcConsts.URL, JdbcConsts.USERNAME, JdbcConsts.PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return conn;
    }


    //获取预处理
    public static PreparedStatement getPreparedStatement(Connection conn, String sql) throws SQLException {
        if (conn != null) {
            return conn.prepareStatement(sql);
        }
        return null;
    }

    //填充参数
    public static void setParams(Object[] params, PreparedStatement pstm) throws SQLException {
        if (pstm != null && params != null) {
            for (int i = 1; i <= params.length; i++) {
                pstm.setObject(i, params[i - 1]);
            }
        }
    }

    //开启事物
    public static void beginTransaction() {
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            connectionThreadLocal.set(conn);
        } catch (SQLException e) {
            connectionThreadLocal.remove();
            close(conn, null, null);
            throw new RuntimeException(e);
        }

    }

    //提交事物
    public static void commitTransaction() {
        Connection conn = connectionThreadLocal.get();
        if (conn != null) {
            try {
                conn.commit();
            } catch (SQLException ex) {
                try {
                    conn.rollback();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                throw new RuntimeException(ex);
            } finally {
                connectionThreadLocal.remove();
                close(conn, null, null);
            }
        }
    }

    //释放资源
    public static void close(Connection conn, PreparedStatement pstm, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pstm != null) {
                pstm.close();
            }
            if (connectionThreadLocal.get() == null) {
                if (conn != null) {
                    conn.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

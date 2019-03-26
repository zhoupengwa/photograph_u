package com.photograph_u.dao;

import com.photograph_u.util.CommUtils;
import com.photograph_u.util.JdbcUtils;
import org.apache.commons.beanutils.BeanUtils;

import java.sql.*;
import java.util.*;

public class BaseDao {
    //查询封装到Bean
    static <T> T queryToBean(String sql, Object[] params, Class<T> clazz) {
        T object = null;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            pstm = JdbcUtils.getPreparedStatement(conn, sql);
            JdbcUtils.setParams(params, pstm);
            rs = pstm.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                object = clazz.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    if (columnValue != null && !("".equals(columnValue))) {
                        BeanUtils.setProperty(object, CommUtils.changeUnderlineToHumpName(columnName), columnValue);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(conn, pstm, rs);
        }
        return object;
    }

    //查询封装到BeanList
    public static <T> List<T> queryToBeanList(String sql, Object[] params, Class<T> clazz) {
        List<T> objectList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            pstm = JdbcUtils.getPreparedStatement(conn, sql);
            JdbcUtils.setParams(params, pstm);
            rs = pstm.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                T object = clazz.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    Object columnValue = rs.getObject(i);
                    String columnName = metaData.getColumnName(i);
                    if (columnValue != null && !("".equals(columnValue))) {
                        BeanUtils.setProperty(object, CommUtils.changeUnderlineToHumpName(columnName), columnValue);
                    }
                }
                objectList.add(object);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(conn, pstm, rs);
        }
        return objectList;
    }

    //查询封装到Map
    public Map<String, Object> queryToMap(String sql, Object[] params) {
        Map<String, Object> resultMap = null;
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            pstm = JdbcUtils.getPreparedStatement(conn, sql);
            JdbcUtils.setParams(params, pstm);
            rs = pstm.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                resultMap = new LinkedHashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    if (columnValue != null && !("".equals(columnValue))) {
                        resultMap.put(CommUtils.changeUnderlineToHumpName(columnName), columnValue);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(conn, pstm, rs);
        }
        return resultMap;
    }

    //查询封装到mapList
    public List<Map<String, Object>> queryToMapList(String sql, Object[] params) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = JdbcUtils.getConnection();
            pstm = JdbcUtils.getPreparedStatement(conn, sql);
            JdbcUtils.setParams(params, pstm);
            rs = pstm.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                Map<String, Object> resultMap = new LinkedHashMap<>();//这里为了保证查询顺序的正确性,不能直接使用HashMap
                for (int i = 1; i <= columnCount; i++) {
                    Object columnValue = rs.getObject(i);
                    String columnName = metaData.getColumnName(i);
                    if (columnValue != null && !("".equals(columnValue))) {
                        resultMap.put(CommUtils.changeUnderlineToHumpName(columnName), columnValue);
                    }
                }
                mapList.add(resultMap);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(conn, pstm, rs);
        }
        return mapList;
    }

    //增删改
    public static int update(String sql, Object[] params) {
        Connection conn = null;
        PreparedStatement pstm = null;
        int count;
        try {
            conn = JdbcUtils.getConnection();
            pstm = JdbcUtils.getPreparedStatement(conn, sql);
            JdbcUtils.setParams(params, pstm);
            count = pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(conn, pstm, null);
        }
        return count;
    }
}

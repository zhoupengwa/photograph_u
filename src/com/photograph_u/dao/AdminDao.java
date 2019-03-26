package com.photograph_u.dao;

import java.util.Map;

public class AdminDao extends BaseDao {
    //查询管理员ID
    public int queryAdminId(String account, String password) {
        String sql = "select id from admin where account=? and password=? and is_deleted=0";
        Map<String, Object> adminIdMap = queryToMap(sql, new Object[]{account, password});
        if (adminIdMap == null) {
            return 0;
        }
        int adminId = (int) adminIdMap.get("id");
        return adminId;
    }
}

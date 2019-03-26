package com.photograph_u.dao;

import java.util.List;
import java.util.Map;

public class AdmireDao extends BaseDao {
    //添加点赞
    public boolean addAdmire(int userId, int noteId) {
        String sql = "insert into admire(user_id,note_id) values(?,?)";
        int count = update(sql, new Object[]{userId, noteId});
        return count == 1;
    }

    //删除点赞
    public boolean deleteAdmire(int userId, int noteId) {
        String sql = "update admire set is_deleted=1 where user_id=? and note_id=? and is_deleted=0";
        int count = update(sql, new Object[]{userId, noteId});
        return count == 1;
    }

    //检查自己对别人帖子的点赞是否存在
    public boolean checkAdmire(int userId, int noteId) {
        String sql = "select count(*) as count from admire where user_id=? and note_id=? and is_deleted=0";
        Map resultMap = queryToMap(sql, new Object[]{userId, noteId});
        long count = (long) resultMap.get("count");
        return count == 1;
    }

    //查询点赞数量
    public int queryAdmireAmountById(int noteId) {
        String sql = "select count(*) as count from admire where note_id=? and is_deleted=0";
        Map<String, Object> resultMap = queryToMap(sql, new Object[]{noteId});
        Object countObject = resultMap.get("count");
        if (countObject == null) {
            return 0;
        }
        return Integer.parseInt(String.valueOf(countObject));
    }

    //查询每个帖子的点赞，包括点赞人的id与昵称
    public List<Map<String, Object>> queryAdmireById(int noteId) {
        String sql = "select user.id,user.nickname from user,(select user_id from admire where note_id=? and is_deleted=0) a where user.id=a.user_id and user.is_deleted=0";
        List<Map<String, Object>> resultMapList = queryToMapList(sql, new Object[]{noteId});
        return resultMapList;
    }

}

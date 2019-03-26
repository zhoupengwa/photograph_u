package com.photograph_u.dao;

import com.photograph_u.domain.OrderInfo;

import java.util.List;
import java.util.Map;

public class FollowDao extends BaseDao {
    //查询摄影师对应的关注
    public List<Map<String, Object>> queryFollows(int photographerId) {
        String sql = "select id,user_id from follow where photographer_id=? and is_deleted=0";
        List<Map<String, Object>> followMapList = queryToMapList(sql, new Object[]{photographerId});
        return followMapList;
    }

    public boolean checkFollow(int userId, int photographerId) {
        String sql = "select count(*) as count from follow where user_id=? and photographer_id=? and is_deleted=0";
        long count = (long) queryToMap(sql, new Object[]{userId, photographerId}).get("count");
        return count == 1;
    }

    //添加关注信息
    public boolean addFollow(int userId, int photographerId) {
        String sql = "insert into follow(user_id,photographer_id) values(?,?)";
        int count = update(sql, new Object[]{userId, photographerId});
        return count == 1;
    }

    //删除关注信息
    public boolean deleteFollow(int userId, int photographerId) {
        String sql = "delete from follow where user_id=? and photographer_id=?";
        int count = update(sql, new Object[]{userId, photographerId});
        return count == 1;
    }

    //查询关注信息
    public List<Map<String, Object>> queryFollowsByUserId(int userId) {
        String sql = "select photographer_id from follow where user_id=? and is_deleted=0";
        List<Map<String, Object>> followMapList = queryToMapList(sql, new Object[]{userId});
        return followMapList;
    }
}

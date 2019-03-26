package com.photograph_u.dao;

import java.util.List;
import java.util.Map;

public class ReviewDao extends BaseDao {
    //查询摄影师对应评价
    public List<Map<String, Object>> queryReviews(int photographerId) {
        String sql = "select id,user_id,content from review where photographer_id=? and is_deleted=0";
        List<Map<String, Object>> reviewMapList = queryToMapList(sql, new Object[]{photographerId});
        return reviewMapList;
    }

    //添加评价
    public boolean addReview(int userId, int photographerId, String  content) {
        String sql = "insert into review(user_id,photographer_id,content) values(?,?,?)";
        int count = update(sql, new Object[]{userId, photographerId, content});
        return count == 1;
    }
}



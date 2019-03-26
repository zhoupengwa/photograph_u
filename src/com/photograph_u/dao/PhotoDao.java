package com.photograph_u.dao;

import java.util.List;
import java.util.Map;

public class PhotoDao extends BaseDao {
    //查询摄影师对应作品
    public List<Map<String, Object>> queryPhotos(int photographerId) {
        String sql = "select id,name from photo where photographer_id=? and is_deleted=0";
        List<Map<String, Object>> photoMapList = queryToMapList(sql, new Object[]{photographerId});
        return photoMapList;
    }

    //添加图片
    public boolean addPhoto(int photographerId, String name) {
        String sql = "insert into photo(photographer_id,name) values(?,?)";
        int count = update(sql, new Object[]{photographerId, name});
        return count == 1;
    }

    //删除图片
    public boolean deletePhoto(int photoId,int photographerId) {
        String sql = "update photo set is_deleted=1 where id=? and photographer_id=? and is_deleted=0";
        int count = update(sql, new Object[]{photoId,photographerId});
        return count == 1;
    }
}

package com.photograph_u.dao;

import com.photograph_u.domain.Style;

import java.util.List;
import java.util.Map;

public class StyleDao extends BaseDao {
    //检查风格是否存在
    public boolean checkStyleById(int styleId) {
        String sql = "select count(*) as count from style where id=? and is_deleted=0";
        Map resultMap = queryToMap(sql, new Object[]{styleId});
        if (resultMap.get("count") == null) {
            return false;
        }
        long count = (long) resultMap.get("count");
        return count == 1;
    }

    //查询所有风格
    public List<Style> queryAllStyles() {
        String sql = "select id,name,image from style where is_deleted=0";
        List<Style> styleList = queryToBeanList(sql, null, Style.class);
        return styleList;
    }

    //添加风格
    public boolean addStyle(String name, String image) {
        String sql = "insert into style(name,image) values(?,?)";
        int count = update(sql, new Object[]{name, image});
        return count == 1;
    }

    //删除风格
    public boolean deleteStyle(int styleId) {
        String sql = "update style set is_deleted=1 where id=? and is_deleted=0";
        int count = update(sql, new Object[]{styleId});
        return count == 1;
    }
}

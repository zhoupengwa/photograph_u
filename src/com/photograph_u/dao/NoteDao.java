package com.photograph_u.dao;

import java.util.List;
import java.util.Map;

public class NoteDao extends BaseDao {
    //添加帖子
    public boolean addNote(int userId, int styleId, String content, String datetime) {
        String sql = "insert into note(user_id,style_id,content,release_time) values(?,?,?,?)";
        Object[] params = {userId, styleId, content, datetime};
        int count = update(sql, params);
        return count == 1;
    }

    //统计总数
    public int countNotes(int styleId) {
        String sql = "select count(*) as count from note where style_id=? and is_deleted=0";
        Map<String, Object> resultMap = queryToMap(sql, new Object[]{styleId});
        Object countObject = resultMap.get("count");
        if (countObject == null) {
            return 0;
        }
        return Integer.parseInt(String.valueOf(countObject));
    }

    //带偏移量查询所有帖子
    public List<Map<String, Object>> queryAllNotesWithOffset(int styleId, int size, int offset) {
        String sql = "select a.id,a.user_id,a.style_id,a.content,a.release_time from note a,(select id from note where style_id=? and is_deleted=0 limit ? offset ?) b where a.id=b.id";
        List<Map<String, Object>> resultMapList = queryToMapList(sql, new Object[]{styleId, size, offset});
        return resultMapList;
    }

    //查询所有帖子
    public List<Map<String, Object>> queryAllNotes(int styleId) {
        String sql = "select id,user_id,style_id,content,release_time from note where style_id=? and is_deleted=0";
        List<Map<String, Object>> resultMapList = queryToMapList(sql, new Object[]{styleId});
        return resultMapList;
    }

    //根据用户ID查询帖子
    public List<Map<String, Object>> queryNotesByUserId(int userId) {
        String sql = "select id,user_id,style_id,content,release_time from note where user_id=? and is_deleted=0";
        List<Map<String, Object>> resultMapList = queryToMapList(sql, new Object[]{userId});
        return resultMapList;
    }

    //删除帖子
    public boolean deleteNote(int userId, int noteId) {
        String sql = "update note set is_deleted=0 where id=? and user_id=? and is_deleted=0";
        int count = update(sql, new Object[]{noteId, userId});
        return count == 1;
    }

    //根据Id查询帖子
    public Map<String, Object> queryNoteById(int noteId) {
        String sql = "select id,user_id,style_id,content,release_time from note where id=? and is_deleted=0";
        Map<String, Object> resultMap = queryToMap(sql, new Object[]{noteId});
        return resultMap;
    }

    //查询最大的Id,并发时可能存在脏数据
    public int queryMaxId() {
        String sql = "select max(id) as max from note";
        Map<String, Object> resultMap = queryToMap(sql, null);
        Object maxId = resultMap.get("max");
        if (maxId == null) {
            return 0;
        }
        return (int) maxId;
    }

    //查询上次事物操作第一次auto_increment得到的Id,同一Connection
    public int queryLastInsertId() {
        String sql = "select last_insert_id() as max";
        Map<String, Object> resultMap = queryToMap(sql, null);
        Object maxId = resultMap.get("max");
        if (maxId == null) {
            return 0;
        }
        return Integer.parseInt(String.valueOf(maxId));//这里本来为long
    }
}

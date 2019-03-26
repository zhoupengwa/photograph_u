package com.photograph_u.dao;

import java.util.List;
import java.util.Map;

public class CommentDao extends BaseDao {
    //添加评论
    public boolean addComment(int userId, int noteId, String content, int father_id) {
        String sql = "insert into comment(user_id,note_id,content,father_id) values(?,?,?,?)";
        Object[] params = {userId, noteId, content, father_id};
        int count = update(sql, params);
        return count == 1;
    }

    //查询帖子的顶级评论数据
    public int queryCommentAmount(int noteId) {
        String sql = "select count(*) as count from comment where note_id=? and father_id=0 and is_deleted=0";
        Map<String, Object> resultMap = queryToMap(sql, new Object[]{noteId});
        Object countObject = resultMap.get("count");
        if (countObject == null) {
            return 0;
        }
        int count = Integer.parseInt(String.valueOf(countObject));
        return count;
    }

    //查询帖子的所有顶级评论
    public List<Map<String, Object>> queryAllFatherCommentsByNoteId(int noteId) {
        String sql = "select a.id,a.content,a.user_id,user.nickname,user.head_image from user,(select id,user_id,content from comment where note_id=? and father_id=0 and is_deleted=0) a where user.id=a.user_id and user.is_deleted=0";
        List<Map<String, Object>> resultMapList = queryToMapList(sql, new Object[]{noteId});
        return resultMapList;
    }

    //查询帖子的顶级评论的所有子评论
    public List<Map<String, Object>> queryAllSonCommentsByNoteId(int noteId, int commentId) {
        String sql = "select a.id,a.content,a.user_id,user.nickname,user.head_image from user,(select id,user_id,content from comment where note_id=? and father_id=? and is_deleted=0) a where user.id=a.user_id and user.is_deleted=0";
        List<Map<String, Object>> resultMapList = queryToMapList(sql, new Object[]{noteId, commentId});
        return resultMapList;
    }

    //检查自己对别人帖子的评论是否存在
    public boolean checkMyComment(int userId, int commentId) {
        String sql = "select count(*) as count from comment where user_id=? and id=? and is_deleted=0";
        Map resultMap = queryToMap(sql, new Object[]{userId, commentId});
        long count = (long) resultMap.get("count");
        return count == 1;
    }

    //检查别人对自己帖子的评论是否存在
    public boolean checkOthersComment(int userId, int commentId) {
        String sql = "select count(*) as count from (select id from note where user_id=? and is_deleted=0) a inner join comment on a.id=comment.note_id where comment.id=? and comment.is_deleted=0";
        Map resultMap = queryToMap(sql, new Object[]{userId, commentId});
        long count = (long) resultMap.get("count");
        return count == 1;
    }


    //删除评论
    public boolean deleteComment(int commentId) {
        String sql = "update comment set is_deleted=1 where id=?";
        int count = update(sql, new Object[]{commentId});
        return count == 1;
    }
}

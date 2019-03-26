package com.photograph_u.dao;

import com.photograph_u.domain.Image;

import java.util.List;

public class ImageDao extends BaseDao {
    //添加图片
    public boolean addImage(int noteId, String name) {
        String sql = "insert into image(note_id,name) values(?,?)";
        int count = update(sql, new Object[]{noteId, name});
        return count == 1;
    }

    //查询帖子对应的图片
    public List<Image> queryImagesByNoteId(int noteId) {
        String sql = "select id,name from image image where note_id=? and is_deleted=0";
        List<Image> imageList = queryToBeanList(sql, new Object[]{noteId}, Image.class);
        return imageList;
    }
}

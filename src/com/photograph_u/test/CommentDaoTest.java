package com.photograph_u.test;

import com.photograph_u.dao.CommentDao;
import org.junit.Test;

public class CommentDaoTest {
    CommentDao dao = new CommentDao();

    @Test
    public void test1() {
        System.out.println(dao.checkOthersComment(2, 5));
    }

    @Test
    public void test2() {
        System.out.println(dao.queryAllFatherCommentsByNoteId(1));
    }
    @Test
    public void test3(){
        System.out.println(dao.queryAllSonCommentsByNoteId(1,5));
    }
}

package com.photograph_u.test;

import com.photograph_u.dao.NoteDao;
import org.junit.Test;

public class NoteDaoTest {
    NoteDao dao = new NoteDao();

    @Test
    public void test1() {
        System.out.println(dao.queryMaxId());
    }
}

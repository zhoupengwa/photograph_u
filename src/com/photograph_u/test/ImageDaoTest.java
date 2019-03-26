package com.photograph_u.test;

import com.google.gson.Gson;
import com.photograph_u.dao.ImageDao;
import org.junit.Test;

public class ImageDaoTest {
    ImageDao dao=new ImageDao();
    @Test
    public void test1(){
        System.out.println(new Gson().toJson(dao.queryImagesByNoteId(2)));
    }
}

package com.photograph_u.test;

import com.photograph_u.dao.StyleDao;
import org.junit.Test;

public class StyleDaoTest {
    StyleDao dao = new StyleDao();

    @Test
    public void test1() {
        System.out.println(dao.checkStyleById(2));
    }
}

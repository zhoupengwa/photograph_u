package com.photograph_u.test;

import com.photograph_u.dao.AdmireDao;
import org.junit.Test;

public class AdmireDaoTest {
    AdmireDao dao = new AdmireDao();

    @Test
    public void test1() {
        System.out.println(dao.checkAdmire(1, 5));
    }

    @Test
    public void test2() {
        System.out.println(dao.queryAdmireAmountById(1));

    }
}

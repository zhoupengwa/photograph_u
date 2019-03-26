package com.photograph_u.test;

import com.photograph_u.service.AdminService;
import org.junit.Test;

public class AdminServiceTest {
    AdminService service = new AdminService();

    @Test
    public void test1() {
        service.login("xiaoyouzi", "xiaoyouzi");
    }
}

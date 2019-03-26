package com.photograph_u.test;

import com.google.gson.Gson;
import com.photograph_u.service.UserService;
import org.junit.Test;

public class UserServiceTest {
    UserService service = new UserService();

    @Test
    public void test1() {
        System.out.println(new Gson().toJson(service.listAllNotes(7)));
    }

    @Test
    public void test2() {
        System.out.println(new Gson().toJson(service.listPhotographerBySchool("西华大学")));

    }
}

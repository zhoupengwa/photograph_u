package com.photograph_u.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.photograph_u.domain.MyResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtil {
    //返回json数据
    public static void sendResponse(HttpServletResponse response, MyResponse myResponse) throws IOException {
        response.getWriter().print(new Gson().toJson(myResponse));
    }

    //返回带日期的json数据，格式化日期[yyyy-MM-dd]
    public static void sendResponseWithDate(HttpServletResponse response, MyResponse myResponse) throws IOException {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        response.getWriter().print(gson.toJson(myResponse));
    }

    //返回带日期和时间的json数据，格式化日期和时间[yyyy-MM-dd HH:mm:ss]
    public static void sendResponseWithDateTime(HttpServletResponse response, MyResponse myResponse) throws IOException {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        response.getWriter().print(gson.toJson(myResponse));
    }
}

package com.photograph_u.filter;

import com.photograph_u.domain.MyResponse;
import com.photograph_u.util.ResponseUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminLoginValidateFilter implements Filter {
     FilterConfig mFilterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.mFilterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Object adminId = request.getSession().getAttribute("admin_id");
        if (adminId == null) {
            MyResponse<String> myResponse = new MyResponse<>();
            myResponse.setCode(3);
            myResponse.setMessage("管理员未登录");
            ResponseUtil.sendResponse(response, myResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        this.mFilterConfig = null;
    }
}

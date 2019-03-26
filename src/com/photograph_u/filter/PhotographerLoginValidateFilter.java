package com.photograph_u.filter;

import com.photograph_u.domain.MyResponse;
import com.photograph_u.util.ResponseUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PhotographerLoginValidateFilter implements Filter {
    FilterConfig mFilterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.mFilterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Object adminId = request.getSession().getAttribute("photographer_user_id");
        if (adminId == null) {
            MyResponse<String> myResponse = new MyResponse<>();
            myResponse.setCode(3);
            myResponse.setMessage("摄影师未登陆");
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

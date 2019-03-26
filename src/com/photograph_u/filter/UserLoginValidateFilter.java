package com.photograph_u.filter;

import com.photograph_u.domain.MyResponse;
import com.photograph_u.util.ResponseUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户登录验证
 */
public class UserLoginValidateFilter implements Filter {
    FilterConfig mFilterConfig;

    @Override
    public void init(FilterConfig filterConfig) {
        this.mFilterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Object userId = request.getSession().getAttribute("user_id");
        if (userId == null) {
            MyResponse<String> myResponse = new MyResponse<>();
            myResponse.setCode(3);
            myResponse.setMessage("用户未登录");
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

package com.photograph_u.filter;

import com.photograph_u.domain.MyResponse;
import com.photograph_u.exception.*;
import com.photograph_u.util.ResponseUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 设置编码、允许带cookie跨域、全局捕获异常
 */
public class GlobalFilter implements Filter {
    FilterConfig mFilterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) {
        this.mFilterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //允许跨域
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        //允许Ajax带cookie请求
        response.setContentType("text/json;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        try {
            response.setHeader("Access-Control-Allow-Credentials", "true");
            //设置编码
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            Throwable cause = e.getCause();//获取正确的异常源
            MyResponse<String> myResponse = new MyResponse<>();
            if ((cause instanceof HttpRequestException)//若属于自定义异常
                    || (cause instanceof MapToBeanConvertException)
                    || (cause instanceof Md5ContentException)
                    || (cause instanceof MessageCodeSendException)
                    || (cause instanceof MyFileUploadException)
                    ) {
                myResponse.setCode(4);
                myResponse.setMessage(cause.getMessage());
                ResponseUtil.sendResponse(response, myResponse);
                e.printStackTrace();
            } else {//属于其他异常
                e.printStackTrace();
                //记录日志
                myResponse.setCode(5);
                myResponse.setMessage("发生未知错误");
                ResponseUtil.sendResponse(response, myResponse);
            }
        }

    }

    @Override
    public void destroy() {
        this.mFilterConfig = null;
    }
}

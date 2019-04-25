package com.itheima.hchat.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@Component
@WebFilter(filterName = "urlFilter",urlPatterns = "/user/test")
public class UrlFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("执行了init方法");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("service之前执行");
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("service返回客户端之前");
        System.exit(0);
    }

    @Override
    public void destroy() {

        System.out.println("执行了destroy方法");
    }
}

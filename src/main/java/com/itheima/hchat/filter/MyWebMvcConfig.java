package com.itheima.hchat.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@SpringBootConfiguration
public class MyWebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private PreIntercepterDemo preIntercepterDemo;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器并配置拦截的路径
        registry.addInterceptor(preIntercepterDemo).addPathPatterns("/user/test");
    }

}

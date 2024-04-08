package com.resume.resu.config;

import com.resume.resu.interceptor.JwtInterceptor;
import com.resume.resu.util.JwtUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebApplicationConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //인터셉터 등록
        registry.addInterceptor(new JwtInterceptor(new JwtUtils())).excludePathPatterns("/login/email");;
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}

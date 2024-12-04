package com.attendance.attendance.settings.config;

import com.attendance.attendance.settings.filter.JwtAuthenticationFilter;
import com.attendance.attendance.settings.service.JwtService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter(JwtService jwtService) {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtAuthenticationFilter(jwtService));
        registrationBean.addUrlPatterns("/api/v1/attendance/*","/api/change_password/*");
        return registrationBean;
    }
}

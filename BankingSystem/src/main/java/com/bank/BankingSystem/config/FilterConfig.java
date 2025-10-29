package com.bank.BankingSystem.config;

import com.bank.BankingSystem.filter.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    public FilterRegistrationBean<JwtFilter> jwtFilter(JwtFilter jwtFilter){
        FilterRegistrationBean<JwtFilter> registrationBean = new  FilterRegistrationBean<>();
        registrationBean.setFilter(jwtFilter);
        registrationBean.addUrlPatterns("/banking/*");
        return registrationBean;
    }
}

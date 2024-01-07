package com.company.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecuredFilterConfig {

    private final TokenFilterConfig tokenFilterConfig;

    @Autowired
    public SecuredFilterConfig(TokenFilterConfig tokenFilterConfig){
        this.tokenFilterConfig = tokenFilterConfig;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(tokenFilterConfig);
        bean.addUrlPatterns("/profile/*");
        return bean;
    }
}

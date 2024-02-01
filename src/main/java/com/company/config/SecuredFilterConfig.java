package com.company.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecuredFilterConfig {

    private final TokenFilter tokenFilterConfig;

    @Autowired
    public SecuredFilterConfig(TokenFilter tokenFilterConfig){
        this.tokenFilterConfig = tokenFilterConfig;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(tokenFilterConfig);
        bean.addUrlPatterns("/api/profiles/*",
                "/api/articles/writer/*",
                "/api/articles/publisher/*",
                "/api/articles/moderator/*",
                "/api/articles/admin/*",
                "/api/articles/user/*",
                "/api/regions/*",
                "/api/categories/*");
        return bean;
    }
}

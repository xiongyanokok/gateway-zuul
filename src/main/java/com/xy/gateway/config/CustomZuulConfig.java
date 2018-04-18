package com.xy.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by xujingfeng on 2017/4/1.
 */
@Configuration
public class CustomZuulConfig {

    @Autowired
    private ZuulProperties zuulProperties;
    
    @Autowired
    private ServerProperties server;

    @Bean
    public CustomRouteLocator routeLocator() {
        return new CustomRouteLocator(server.getServletPrefix(), zuulProperties);
    }

}

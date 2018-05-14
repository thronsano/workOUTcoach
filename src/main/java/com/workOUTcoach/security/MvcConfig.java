package com.workOUTcoach.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/reset").setViewName("reset");
        registry.addViewController("/forgot").setViewName("forgot");
        registry.addViewController("/timeTable").setViewName("timeTable");
        registry.addViewController("/clientList").setViewName("clientList");
        registry.addViewController("/clientProfile").setViewName("clientProfile");
    }

}
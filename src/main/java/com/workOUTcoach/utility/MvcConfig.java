package com.workOUTcoach.utility;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/home").setViewName("home");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/reset").setViewName("reset");
        registry.addViewController("/forgot").setViewName("forgot");
        registry.addViewController("/clientList").setViewName("clientList");
        registry.addViewController("/clientProfile").setViewName("clientProfile");
        registry.addViewController("/settings").setViewName("accountSettings");
        registry.addViewController("/addAppointment").setViewName("addAppointment");
        registry.addViewController("/payments").setViewName("payments");
    }
}
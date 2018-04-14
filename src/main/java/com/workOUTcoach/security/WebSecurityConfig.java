package com.workOUTcoach.security;

import com.workOUTcoach.utility.DatabaseConnector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/",
                        "/home",
                        "/*.css",
                        "*")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        String login="";
        String password="";
        DatabaseConnector databaseConnector = new DatabaseConnector();
        ResultSet resultSet= DatabaseConnector.executeQuery("SELECT login FROM USERS ");

        try {
            if(resultSet.next()){
                login=resultSet.getString(1);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

       resultSet= DatabaseConnector.executeQuery("SELECT password FROM USERS ");


        try {
            if(resultSet.next()){
                password=resultSet.getString(1);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username(login)
                        .password(password)
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
}
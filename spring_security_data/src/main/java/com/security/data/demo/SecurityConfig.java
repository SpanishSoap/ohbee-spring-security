package com.security.data.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
    @Autowired
    UserDetailsService userService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                .antMatchers("/admin/**").hasRole("admin")
                .antMatchers("/user/**").hasRole("user")
                .anyRequest().authenticated()
                .and()
                //前后端分离 , 登录参数定义
                .formLogin()
                .loginProcessingUrl("/doLogin") //定义登录请求的地址
                .usernameParameter("account") //定义账号
                .passwordParameter("password") //定义密码
                //登录成功 , 返回json处理
                .successHandler(( request, response,  authentication)->{
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.write(new ObjectMapper().writeValueAsString(authentication));
                    writer.flush();
                    writer.close();
                })
                //登录失败 , 返回json处理
                .failureHandler(( request, response,  exception)->{
                            response.setContentType("application/json;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(exception.getMessage());
                            writer.flush();
                            writer.close();
                        }
                )
                //未认证处理 , 返回json处理
                .and().exceptionHandling()
                .authenticationEntryPoint( (request,  response, authException)->{
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.write("用户未登录!!");
                   // writer.write(new ObjectMapper().writeValueAsString(authException.getMessage()));
                    writer.flush();
                    writer.close();
                })
                //注销登录定义
                .and().logout()
                .logoutUrl("/logout") //定义登出请求地址
                .logoutSuccessHandler(( request,  response, authentication)->{
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.write("登出成功!!");
                    writer.flush();
                    writer.close();
                })
                .permitAll()
                //记住我功能
                .and()
                .rememberMe().rememberMeParameter("valid").key("Friends").tokenValiditySeconds(60)
                .and()
                .csrf().disable();
    }
}

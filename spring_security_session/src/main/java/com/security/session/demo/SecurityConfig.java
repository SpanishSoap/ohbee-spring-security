package com.security.session.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Properties;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Bean
    Producer verifyCode() {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(new Config(new Properties()));
        return defaultKaptcha;
    }

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
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**","/assert/**","/login.html");
    }

    @Autowired
    private VerificationCodeFilter verificationCodeFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       // http.addFilterBefore(verificationCodeFilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers("/verificationCode").permitAll()
                .antMatchers("/admin/**").hasRole("admin")
                .antMatchers("/user/**").hasRole("user")
                .anyRequest().authenticated()
                .and()
                //前后端分离 , 登录参数定义
                .formLogin()
                .loginProcessingUrl("/doLogin") //定义登录请求的地址
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
                    writer.write(authException.getMessage());
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
//                .and()
//                .rememberMe().key("Friends")
                .and()
                .csrf().disable()
                .sessionManagement()
                .maximumSessions(1).expiredSessionStrategy((event)->{
                        HttpServletResponse response = event.getResponse();
                        response.setContentType("application/json;charset=utf-8");
                        response.getWriter().print(
                                "用户已在其他地址登录 , 若不是本人登录请重新登录!!");
                        response.flushBuffer();
        });
    }
}

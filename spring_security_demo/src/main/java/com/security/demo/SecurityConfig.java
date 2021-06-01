package com.security.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.inMemoryAuthentication()
      .withUser("Chandler").password("Chandler's password").roles("admin")
      .and()
      .withUser("Ross").password("Ross's password").roles("user");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**","/assert/**");
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/login.html")
//                .permitAll()
//                .and()
//                .csrf().disable();
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                //登录参数定义
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/doLogin") //定义登录请求的地址
                .usernameParameter("account") //定义账号
                .passwordParameter("passwd") //定义密码
                //登录成功回调
                .defaultSuccessUrl("/home")//客户端重定向
                //.successForwardUrl("/home")//服务端跳转
                //.successHandler() // 前后端分离时处理操作
                //登录失败回调定义
//                .failureForwardUrl("/login.html")
                .failureUrl("/login.html")
                //注销登录定义
                .and().logout()
                .logoutUrl("/logout") //定义登出请求地址
                //.logoutRequestMatcher(new AntPathRequestMatcher("/logout","POST"))//将登出请求地址修改为post请求
                .logoutSuccessUrl("/index") // 登出成功后的访问地址
                .deleteCookies() //默认不用设置系统默认登出调用
                .clearAuthentication(true) //默认不用设置系统默认登出调用
                .invalidateHttpSession(true) //默认不用设置系统默认登出调用
                .permitAll()
                .and()
                .csrf().disable();
    }
}

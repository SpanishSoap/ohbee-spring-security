package com.security.filter.demo;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class VerificationCodeFilter extends GenericFilterBean  {
    private String defaultFilterProcessUrl = "/doLogin";
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if ("POST".equalsIgnoreCase(request.getMethod()) && defaultFilterProcessUrl.equals(request.getServletPath())) {
            String userCode = request.getParameter("code");
            String sysVerificationCode = (String) request.getSession().getAttribute("sysVerificationCode");
            if(StringUtils.isEmpty(sysVerificationCode))
            {
                responseInfo(response,"获取验证码失败，请刷新页面!");
                return;
            }
            if (StringUtils.isEmpty(userCode)) {
                responseInfo(response, "验证码不能为空!");
                return;
            }
            if (!userCode.toLowerCase().equals(sysVerificationCode.toLowerCase())) {
                responseInfo(response,"验证码错误!");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private void responseInfo(ServletResponse response,String message) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(message);
        writer.flush();
        writer.close();
    }
}

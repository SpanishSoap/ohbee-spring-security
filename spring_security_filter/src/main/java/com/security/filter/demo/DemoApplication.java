package com.security.filter.demo;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@SpringBootApplication
@RestController
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Autowired
    private Producer producer;

    @GetMapping("/verificationCode")
    public void verificationCode(HttpSession session, HttpServletResponse response) throws IOException {
        String text = producer.createText();
        BufferedImage image = producer.createImage(text);
        session.setAttribute("sysVerificationCode",text);
        response.setContentType("image/jpeg");
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image,"jpg",outputStream);
    }

    @GetMapping("/hi")
    public String sayHi(){
        return "Hi !!!   I'm Joey";
    }

    @RequestMapping("/admin/hi")
    public String admin(){
        return "Hi !!! I'm Monica";
    }

    @RequestMapping("/user/hi")
    public String user(){ return "Hi !!! I'm Rachel";
    }

    @RequestMapping("/loginUserInfo")
    public Object loginUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

}

package com.security.data.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    @GetMapping("hi")
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
}

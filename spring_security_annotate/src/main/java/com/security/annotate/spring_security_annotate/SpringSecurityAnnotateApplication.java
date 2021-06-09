package com.security.annotate.spring_security_annotate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;

@SpringBootApplication
@RestController
public class SpringSecurityAnnotateApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityAnnotateApplication.class, args);
    }

    @GetMapping("hi")
    public String sayHi(){
        return "Hi !!!   I'm Joey";
    }

    @RequestMapping("/admin/hi")
    @PreAuthorize("hasAuthority('admin')")
    public String admin(){
        return "Hi !!! I'm Monica";
    }


    @RequestMapping("/user/hi")
    @PreAuthorize("hasAuthority('user')")
    public String user(){ return "Hi !!! I'm Rachel";
    }

}

package com.security.filter.demo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;


public class Role implements GrantedAuthority {
    @Getter@Setter
    private Long id;
    @Getter@Setter
    private String name;
    @Setter
    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}

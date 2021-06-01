package com.security.session.demo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select id, username, password, account_non_expired, account_non_locked, credentials_non_expired, enabled from user where username = #{username}")
    User selectByUsername(String username);
}
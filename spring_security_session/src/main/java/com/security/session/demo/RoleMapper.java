package com.security.session.demo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface RoleMapper {
    @Select("select a.* from role a inner  join  user_role b on  a.id=b.role_id  where b.user_id = #{id}")
    List<Role> selectByUserId(Long id);
}


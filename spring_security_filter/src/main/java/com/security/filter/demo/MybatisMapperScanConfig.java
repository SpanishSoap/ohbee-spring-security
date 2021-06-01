package com.security.filter.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.security.filter.demo")
public class MybatisMapperScanConfig {}

package com.security.single.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.security.single.demo")
public class MybatisMapperScanConfig {}

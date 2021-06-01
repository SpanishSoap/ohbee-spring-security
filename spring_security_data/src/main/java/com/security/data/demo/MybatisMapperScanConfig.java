package com.security.data.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.security.data.demo")
public class MybatisMapperScanConfig {}

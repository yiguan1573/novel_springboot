package com.yiguan.novel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement//开启事务
@MapperScan("com.yiguan.novel.mapper")
@SpringBootApplication
public class Novel01Application{
    public static void main(String[] args) {
        SpringApplication.run(Novel01Application.class, args);
    }
}

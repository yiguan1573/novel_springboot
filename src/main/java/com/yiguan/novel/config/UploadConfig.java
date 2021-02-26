package com.yiguan.novel.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@ComponentScan
@Configuration
public class UploadConfig implements WebMvcConfigurer {

    @Autowired
    PreReadUploadConfig uploadConfig;


    //将/static/映射为图片路径
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("file:///"+uploadConfig.getUploadPath());
//        registry.addResourceHandler("/static/**").addResourceLocations("file:/usr/local/dev/image/");
    }

//    public void addCorsMappings(CorsRegistry registry) {
//        //本应用的所有方法都会去处理跨域请求
//        registry.addMapping("/**")
//                //允许远端访问的域名
//                .allowedOrigins("http://localhost:8080","http://localhost:8088")
//                //允许请求的方法("POST", "GET", "PUT", "OPTIONS", "DELETE")
//                .allowedMethods("*")
//                //允许请求头
//                .allowedHeaders("*");
//    }
}

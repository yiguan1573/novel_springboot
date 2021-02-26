package com.yiguan.novel.controller;

import com.yiguan.novel.utils.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class VerificationController {


    //验证token是否过期
    @CrossOrigin
    @PostMapping("/verification")
    @ResponseBody
    public Map<String,Object> TokenVerification(@RequestHeader("token") String token) throws Exception {
        Map<String,Object> map = new HashMap<>();
        boolean isExpires = JwtUtil.verifyToken(token);
        if(isExpires == true){
            map.put("status",201);//token过期
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
        }else{
            map.put("status",200);//token未过期
        }
        return  map;
    }


}

package com.yiguan.novel.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiguan.novel.bean.Comment;
import com.yiguan.novel.bean.Novel;
import com.yiguan.novel.bean.User;
import com.yiguan.novel.service.CommentService;
import com.yiguan.novel.service.NovelService;
import com.yiguan.novel.service.UserService;
import com.yiguan.novel.utils.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    NovelService novelService;
    @Autowired
    CommentService commentService;

    @Transactional
    //解决跨域问题
    @CrossOrigin
    @PostMapping("/register")
    public  Map<String,Object> register(@RequestBody User user){
        Map<String,Object> map = new HashMap<>();
        //查重
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("username",user.getUserName());
        User user1 = userService.getOne(queryWrapper);
        System.out.println(user1);
        if(user1 == null){
            map.put("status",200);
            userService.save(user);
            return map;
        }else{
            map.put("status",400);
            return map;
        }
    }


    @CrossOrigin
    @GetMapping("/logout")
    public Map<String,Object> logout(){
        Map<String,Object> map = new HashMap<>();
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        map.put("status",200);
        return map;
    }

    @Transactional
    @CrossOrigin
    @PostMapping("/login")
    public Map<String,Object> login(@RequestBody User user){
        String userName = user.getUserName();
        String password = user.getPassword();
        Map<String,Object> result = new HashMap<>();

        //获取Subnject
        Subject subject = SecurityUtils.getSubject();
        //封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(userName,password);//该token是用来验证登陆的，并未传给客户端
        //执行登陆方法
        try {
            subject.login(token);
            //登陆成功

            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper
                    .eq("username",token.getUsername());
            User user1 = userService.getOne(queryWrapper);

            //判断该用户是否有头像
            if(user1.getImage() != null){
                result.put("url",user1.getImage());
            }else{
                result.put("url","");
            }

            //判断该账户是否有token或者token是否过期
            if(user1.getToken() == null || JwtUtil.verifyToken(user1.getToken())){
                String Token = JwtUtil.createToken(user1.getId(), userName);//该token才是真的token
                //保存token
                user1.setToken(Token);
                userService.updateById(user1);
                result.put("token",Token);
                result.put("status",200);
            }else {
                result.put("token",user1.getToken());
                result.put("status",200);
            }
            return result;
        }catch (UnknownAccountException e){
            //登录失败，用户名不存在
            result.put("msg","用户名不存在");
            result.put("status",400);
            return result;
        }catch (IncorrectCredentialsException e){
            result.put("msg","密码错误");
            result.put("status",401);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }

    //更新用户名和密码
    @Transactional
    @CrossOrigin
    @PostMapping("/updateUser")
    public Map<String,Object> updateUser(@RequestHeader("token")String token,@RequestBody User user){
        Map<String,Object> map = new HashMap<>();
        try {
            //从token中获取到id
            Long id = JwtUtil.parseToken(token).getClaim("userId").asLong();
            //通过id查找
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper
                    .eq("id",id);
            User user1 = userService.getOne(queryWrapper);
            //生成新的token
            String Token = JwtUtil.createToken(id, user.getUserName());

            user1.setToken(Token);
            user1.setUserName(user.getUserName());
            user1.setPassword(user.getPassword());
            user1.setImage(user.getImage());
            userService.updateById(user1);

            //更新comment表冗余的用户数据
            QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
            commentQueryWrapper.eq("user_id",id);
            List<Comment> list=  commentService.list(commentQueryWrapper);
            for (Comment comment : list){
                comment.setUserName(user.getUserName());
            }
            commentService.updateBatchById(list);


            map.put("token",Token);
            map.put("status",200);
        } catch (UnsupportedEncodingException e) {
            map.put("status",400);
        } catch (Exception e) {
            map.put("status",400);
        }

        return  map;
    }

    //获取用户上传
    @Transactional
    @CrossOrigin
    @GetMapping("/myUploadPC")
    public List<Novel> getMyUploadPC(@RequestHeader("token") String token) {
        List<Novel> list = new ArrayList<>();
        //从token中获取到id
        try {
            Long id = JwtUtil.parseToken(token).getClaim("userId").asLong();
            QueryWrapper<Novel> queryWrapper = new QueryWrapper<>();
            queryWrapper
                    .eq("user_id",id)
                    .last("limit 4");
            list = novelService.list(queryWrapper);
            return list;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    //获取用户上传
    @Transactional
    @CrossOrigin
    @GetMapping("/myUploadPhone")
    public List<Novel> getMyUploadPhone(@RequestHeader("token") String token) {
        List<Novel> list = new ArrayList<>();
        //从token中获取到id
        try {
            Long id = JwtUtil.parseToken(token).getClaim("userId").asLong();
            QueryWrapper<Novel> queryWrapper = new QueryWrapper<>();
            queryWrapper
                    .eq("user_id",id)
                    .last("limit 3");
            list = novelService.list(queryWrapper);
            return list;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}

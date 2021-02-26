package com.yiguan.novel.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yiguan.novel.bean.Comment;
import com.yiguan.novel.bean.Novel;
import com.yiguan.novel.bean.User;
import com.yiguan.novel.config.PreReadUploadConfig;
import com.yiguan.novel.service.CommentService;
import com.yiguan.novel.service.NovelService;
import com.yiguan.novel.service.UserService;
import com.yiguan.novel.utils.FileUtil;
import com.yiguan.novel.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//用户头像上传
@RestController
public class ImgUploadController {

    @Autowired
    UserService userService;
    @Autowired
    PreReadUploadConfig uploadConfig;
    @Autowired
    NovelService novelService;
    @Autowired
    CommentService commentService;



    @Transactional
    @CrossOrigin
    @PostMapping("/updateImg")
    public Map<String,Object> ImgUpload(@RequestParam("file") MultipartFile headerImg, @RequestParam("userName") String userName, @RequestParam("previousUrl") String previousUrl, @RequestHeader("token") String token){
        Map<String,Object> map = new HashMap<>();
        if (headerImg.isEmpty()) {
            map.put("status", 400);
            return map;
        }

        //重命名
        String fileName = headerImg.getOriginalFilename();
        fileName = FileUtil.renameToUUID(fileName);
        if (fileName == null) {
            map.put("status", 400);
            return map;
        }
        //文件上传
        try {
            FileUtil.uploadFiles(headerImg.getBytes(), uploadConfig.getUploadPath(), fileName);//uploadConfig.getUploadPath()可在yml文件中设置
        } catch (Exception e) {
            map.put("status", 400);
            return map;
        }

        String url = "/static/" + fileName;

        //更新image路径
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper
                .set("image",url)
                .eq("username", userName);
        userService.update(userUpdateWrapper);

        //删除之前的图片
        if(!previousUrl.equals("null")){//注意previousUrl为字符串的null
            boolean isDelete = FileUtil.deleteImg(uploadConfig.getUploadPath(),previousUrl.substring(previousUrl.lastIndexOf("/") + 1));
        }

        //更新comment表冗余的用户数据
        Long userId;
        try {
            userId = JwtUtil.parseToken(token).getClaim("userId").asLong();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            map.put("status", 400);
            return map;
        }
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("user_id",userId);
        List<Comment> list=  commentService.list(commentQueryWrapper);
        for (Comment comment : list){
            comment.setUserImg(url);
        }
        commentService.updateBatchById(list);

        map.put("status",200);
        map.put("url",url);

        return map;
    }

    @Transactional
    @CrossOrigin
    @PostMapping("/uploadNovelImg")
    public Map<String,Object> NovelImgUpload(@RequestParam("file") MultipartFile headerImg, @RequestParam("novelName") String novelName){
        Map<String,Object> map = new HashMap<>();
        if (headerImg.isEmpty()) {
            map.put("status", 200);
            return map;
        }
        //重命名
        String fileName = headerImg.getOriginalFilename();
        fileName = FileUtil.renameToUUID(fileName);
        if (fileName == null) {
            map.put("status", 400);
            return map;
        }
        //文件上传
        try {
            FileUtil.uploadFiles(headerImg.getBytes(), uploadConfig.getUploadPath(), fileName);//uploadConfig.getUploadPath()可在yml文件中设置
        } catch (Exception e) {
            map.put("status", 400);
            return map;
        }

        String url = "/static/" + fileName;

        //更新image路径
        UpdateWrapper<Novel> novelUpdateWrapper = new UpdateWrapper<>();
        novelUpdateWrapper
                .set("novel_img",url)
                .eq("novel_name", novelName);
        novelService.update(novelUpdateWrapper);

        map.put("status",200);

        return map;
    }

}

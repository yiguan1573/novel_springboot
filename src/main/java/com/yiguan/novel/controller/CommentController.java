package com.yiguan.novel.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yiguan.novel.bean.Comment;
import com.yiguan.novel.service.CommentService;
import com.yiguan.novel.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class CommentController {
    @Autowired
    CommentService commentService;


    @Transactional
    @CrossOrigin
    @PostMapping("/comment")
    public Long comment(@RequestHeader("token") String token,@RequestBody Comment comment){
        Long userId;
        try {
            userId = JwtUtil.parseToken(token).getClaim("userId").asLong();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        comment.setUserId(userId);
        //获取现在的时间
        Date date=new Date();//先获取一个Date对象
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//创建一个格式化日期对象
        String punchTime = simpleDateFormat.format(date);//格式化后的时间
        comment.setCommentTime(punchTime);
        comment.setCommentPnum(0L);
        comment.setCommentNnum(0L);
        commentService.save(comment);
        if(comment.getParentId() != null){
            UpdateWrapper<Comment> commentUpdateWrapper = new UpdateWrapper<>();
            commentUpdateWrapper
                    .set("isHavingReplay",1)
                    .eq("comment_id", comment.getParentId());
            commentService.update(commentUpdateWrapper);
        }
         return comment.getCommentId();//将comment_id返回
    }

    @Transactional
    @CrossOrigin
    @GetMapping("/getComment")
    public List<Comment> getComment(@RequestParam("novelId") Long novelId){
        QueryWrapper<Comment> queryWrapper =new QueryWrapper<>();
        queryWrapper
                .orderBy(true, false,"comment_id")//按comment_id倒序查询
                .eq("novel_id",novelId)
                .isNull("parent_id");
        return commentService.list(queryWrapper);
    }

    @Transactional
    @CrossOrigin
    @GetMapping("/getRepaly")
    public List<Comment> getRepaly(@RequestParam("parentId") Long parentId){
        QueryWrapper<Comment> queryWrapper =new QueryWrapper<>();
        queryWrapper
                .orderBy(true, false,"comment_id")//按comment_id倒序查询
                .eq("parent_id",parentId);
        return commentService.list(queryWrapper);
    }
}

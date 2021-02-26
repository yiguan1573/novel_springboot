package com.yiguan.novel.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yiguan.novel.bean.Comment;
import com.yiguan.novel.bean.CommentAction;
import com.yiguan.novel.service.CommentActionService;
import com.yiguan.novel.service.CommentService;
import com.yiguan.novel.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
public class CommentActionController {
    @Autowired
    CommentService commentService;
    @Autowired
    CommentActionService commentActionService;

    @Transactional
    @CrossOrigin
    @GetMapping("/commentAction")
    public List<CommentAction> getcommentAction(@RequestHeader("token") String token, @RequestParam("novelId") Long novelId){
        Long userId;
        try {
            userId = JwtUtil.parseToken(token).getClaim("userId").asLong();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        //从comment表中找到所有的评论
//        QueryWrapper<Comment> commentQueryWrapper =new QueryWrapper<>();
//        commentQueryWrapper.select("comment_id").eq("novel_id",novelId);
//        List<Comment> commentList = commentService.list(commentQueryWrapper);
//        if(commentList.size() == 0){
//            return null;
//        }

        //从commentAction表中找到用户在该小说中的点赞点踩数据
        QueryWrapper<CommentAction> commentActionQueryWrapper =new QueryWrapper<>();
        commentActionQueryWrapper
                .eq("novel_id",novelId)
                .eq("user_id",userId);
        List<CommentAction> commentActionList = commentActionService.list(commentActionQueryWrapper);
        return commentActionList;


    }

    @Transactional
    @CrossOrigin
    @GetMapping("/commentUp")
    public void commentUp(@RequestHeader("token") String token,@RequestParam("novelId") Long novelId,@RequestParam("commentId") Long commentId){
        Long userId;
        try {
            userId = JwtUtil.parseToken(token).getClaim("userId").asLong();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        QueryWrapper<CommentAction> commentActionQueryWrapper =new QueryWrapper<>();
        commentActionQueryWrapper
                .eq("user_id",userId)
                .eq("comment_id",commentId);
        CommentAction commentAction = commentActionService.getOne(commentActionQueryWrapper);
        Comment comment =  commentService.getById(commentId);
        if(commentAction == null){
            CommentAction commentAction1 = new CommentAction(commentId,userId,novelId,1,0);
            commentActionService.save(commentAction1);
            comment.setCommentPnum(comment.getCommentPnum()+1);
            commentService.updateById(comment);
        }else{
            if(commentAction.getCommentPositive() == 0 && commentAction.getCommentNegative() == 0){
                commentAction.setCommentPositive(1);
                comment.setCommentPnum(comment.getCommentPnum()+1);
            }else if(commentAction.getCommentPositive() == 1 && commentAction.getCommentNegative() == 0){
                commentAction.setCommentPositive(0);
                comment.setCommentPnum(comment.getCommentPnum()-1);
            }else if(commentAction.getCommentPositive() == 0 && commentAction.getCommentNegative() == 1){//防止同时出现点赞和点踩
                commentAction.setCommentPositive(1);
                comment.setCommentPnum(comment.getCommentPnum()+1);
                commentAction.setCommentNegative(0);
                comment.setCommentNnum(comment.getCommentNnum()-1);
            }
            UpdateWrapper<CommentAction> updateWrapper = new UpdateWrapper<>();
            updateWrapper
                    .eq("user_id",userId)
                    .eq("comment_id",commentId);
            commentActionService.update(commentAction,updateWrapper);
            commentService.updateById(comment);
        }
    }

    @Transactional
    @CrossOrigin
    @GetMapping("/commentDown")
    public void commentDown(@RequestHeader("token") String token,@RequestParam("novelId") Long novelId,@RequestParam("commentId") Long commentId){
        Long userId;
        try {
            userId = JwtUtil.parseToken(token).getClaim("userId").asLong();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        QueryWrapper<CommentAction> commentActionQueryWrapper =new QueryWrapper<>();
        commentActionQueryWrapper
                .eq("user_id",userId)
                .eq("comment_id",commentId);
        CommentAction commentAction = commentActionService.getOne(commentActionQueryWrapper);
        Comment comment =  commentService.getById(commentId);
        if(commentAction == null){
            CommentAction commentAction1 = new CommentAction(commentId,userId,novelId,0,1);
            commentActionService.save(commentAction1);
            comment.setCommentPnum(comment.getCommentNnum()+1);
            commentService.updateById(comment);
        }else{
            if(commentAction.getCommentNegative() == 0 && commentAction.getCommentPositive() == 0){
                commentAction.setCommentNegative(1);
                comment.setCommentNnum(comment.getCommentNnum()+1);
            }else if(commentAction.getCommentNegative() == 1 && commentAction.getCommentPositive() == 0){
                commentAction.setCommentNegative(0);
                comment.setCommentNnum(comment.getCommentNnum()-1);
            }else if(commentAction.getCommentNegative() == 0 && commentAction.getCommentPositive() == 1){
                commentAction.setCommentNegative(1);
                comment.setCommentNnum(comment.getCommentNnum()+1);
                commentAction.setCommentPositive(0);
                comment.setCommentPnum(comment.getCommentPnum()-1);
            }
            UpdateWrapper<CommentAction> updateWrapper = new UpdateWrapper<>();
            updateWrapper
                    .eq("user_id",userId)
                    .eq("comment_id",commentId);
            commentActionService.update(commentAction,updateWrapper);
            commentService.updateById(comment);
        }
    }
}

package com.yiguan.novel.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yiguan.novel.bean.Novel;
import com.yiguan.novel.bean.UserAction;
import com.yiguan.novel.service.NovelService;
import com.yiguan.novel.service.UserActionService;
import com.yiguan.novel.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserActionController {
    @Autowired
    UserActionService userActionService;
    @Autowired
    NovelService novelService;

    @Transactional
    @CrossOrigin
    @GetMapping("/myCollection")
    public List<Novel> getMyCollection(@RequestHeader("token") String token, @RequestParam("num") int num){
        List<Novel> listNovel = new ArrayList<>();
        List<UserAction> listUserAction = new ArrayList<>();
        Long id;
        //查看表中是否有userAction的数据
        try {
            id = JwtUtil.parseToken(token).getClaim("userId").asLong();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        QueryWrapper<UserAction> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("user_id",id);
        listUserAction = userActionService.list(queryWrapper);
        if(listUserAction.isEmpty()){
            return listNovel;
        }else{
            for(UserAction userAction: listUserAction){
                if(userAction.getUserCollection() == 1){ //为1即该小说已收藏
                    QueryWrapper<Novel> novelQueryWrapper = new QueryWrapper<>();
                    novelQueryWrapper.eq("novel_id",userAction.getNovelId());
                    listNovel.add(novelService.getOne(novelQueryWrapper));
                    if(listNovel.size() == num){
                        return listNovel;
                    }
                }
            }
            return listNovel;
        }
    }

    @Transactional
    @CrossOrigin
    @GetMapping("/thumbsup")
    public void thumbsup(@RequestHeader("token") String token,@RequestParam("novelId") Long novelId){
        Long userId;
        try {
            userId = JwtUtil.parseToken(token).getClaim("userId").asLong();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        QueryWrapper<UserAction> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("user_id",userId)
                .eq("novel_id",novelId);
        UserAction userAction = userActionService.getOne(queryWrapper);
        Novel novel = novelService.getById(novelId);
        if(userAction == null){
            UserAction userAction1 = new UserAction(userId,novelId,1,0);
            userActionService.save(userAction1);
            novel.setNovelThumbsup(novel.getNovelThumbsup()+1);
            novelService.updateById(novel);
        }else {
            if (userAction.getUserThumbsup()==0){
                userAction.setUserThumbsup(1);
                novel.setNovelThumbsup(novel.getNovelThumbsup()+1);
            }else{
                userAction.setUserThumbsup(0);
                novel.setNovelThumbsup(novel.getNovelThumbsup()-1);
            }
            UpdateWrapper<UserAction> updateWrapper = new UpdateWrapper<>();
            updateWrapper
                    .eq("user_id",userId)
                    .eq("novel_id",novelId);
            userActionService.update(userAction,updateWrapper);
            novelService.updateById(novel);
        }

    }

    @Transactional
    @CrossOrigin
    @GetMapping("/collection")
    public void collection(@RequestHeader("token") String token,@RequestParam("novelId") Long novelId){
        Long userId;
        try {
            userId = JwtUtil.parseToken(token).getClaim("userId").asLong();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        QueryWrapper<UserAction> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("user_id",userId)
                .eq("novel_id",novelId);
        UserAction userAction = userActionService.getOne(queryWrapper);
        Novel novel = novelService.getById(novelId);
        if(userAction == null){
            UserAction userAction1 = new UserAction(userId,novelId,0,1);
            userActionService.save(userAction1);
            novel.setNovelCollection(novel.getNovelCollection()+1);
            novelService.updateById(novel);
        }else {
            if (userAction.getUserCollection()==0){
                userAction.setUserCollection(1);
                novel.setNovelCollection(novel.getNovelCollection()+1);
            }else{
                userAction.setUserCollection(0);
                novel.setNovelCollection(novel.getNovelCollection()-1);
            }
            UpdateWrapper<UserAction> updateWrapper = new UpdateWrapper<>();
            updateWrapper
                    .eq("user_id",userId)
                    .eq("novel_id",novelId);
            userActionService.update(userAction,updateWrapper);
            novelService.updateById(novel);
        }

    }

    @Transactional
    @CrossOrigin
    @GetMapping("/userScore")
    public void userSore(@RequestHeader("token") String token,@RequestParam("novelId") Long novelId,@RequestParam("userScore") Float userScore){
        Long userId;
        try {
            userId = JwtUtil.parseToken(token).getClaim("userId").asLong();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        QueryWrapper<UserAction> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("user_id",userId)
                .eq("novel_id",novelId);
        UserAction userAction = userActionService.getOne(queryWrapper);
        if(userAction == null){
            UserAction userAction1 = new UserAction(userId,novelId,0,0,userScore);
            userActionService.save(userAction1);
            //更新该小说的平均评分
//            QueryWrapper<UserAction> queryWrapper1 = new QueryWrapper<>();
//            queryWrapper1.eq("novel_id",novelId);
//            List<UserAction> userActions = userActionService.list(queryWrapper1);
//            int total = 0;
//            for(UserAction userAction2 : userActions){
//                if(userAction2.getUserScore() != null){
//
//                }
//            }
            Float avgScore = userActionService.getAvgScore(novelId);
            UpdateWrapper<Novel> novelUpdateWrapper = new UpdateWrapper<>();
            novelUpdateWrapper
                    .set("novel_score",avgScore)
                    .eq("novel_id", novelId);
            novelService.update(novelUpdateWrapper);
        }else {
            UpdateWrapper<UserAction> userActionUpdateWrapper = new UpdateWrapper<>();
            userActionUpdateWrapper
                    .eq("user_id",userId)
                    .eq("novel_id",novelId)
                    .set("user_score",userScore);
            userActionService.update(userActionUpdateWrapper);
            Float avgScore = userActionService.getAvgScore(novelId);
            UpdateWrapper<Novel> novelUpdateWrapper = new UpdateWrapper<>();
            novelUpdateWrapper
                    .set("novel_score",avgScore)
                    .eq("novel_id", novelId);
            novelService.update(novelUpdateWrapper);
        }
    }
}

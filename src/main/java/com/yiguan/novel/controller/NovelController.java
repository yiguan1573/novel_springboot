package com.yiguan.novel.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiguan.novel.bean.CommentAction;
import com.yiguan.novel.bean.Novel;
import com.yiguan.novel.bean.UserAction;
import com.yiguan.novel.service.CommentActionService;
import com.yiguan.novel.service.NovelService;
import com.yiguan.novel.service.UserActionService;
import com.yiguan.novel.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class NovelController {
    @Autowired
    NovelService novelService;
    @Autowired
    UserActionService userActionService;
    @Autowired
    CommentActionService commentActionService;

    @Transactional
    @CrossOrigin
    @PostMapping("/uploadNovel")
    public Map<String,Object> register(@RequestHeader String token,@RequestBody Novel novel){
        Map<String,Object> map = new HashMap<>();

        //查重
        QueryWrapper<Novel> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("novel_name",novel.getNovelName());
        Novel novelSelect = novelService.getOne(queryWrapper);
        if(novelSelect == null){
            //从token中获取到id
            try {
                Long id = JwtUtil.parseToken(token).getClaim("userId").asLong();
                novel.setUserId(id);
                if(novel.getNovelNum() == null){
                    novel.setNovelNum("未知");
                }
                novel.setNovelScore(0.00F);
                novel.setNovelCollection(0L);
                novel.setNovelThumbsup(0L);
                novelService.save(novel);
                map.put("status",200);
                return map;
            } catch (UnsupportedEncodingException e) {
                map.put("status",401);//转码异常
                return map;
            }

        }else{
            map.put("status",400);//该小说已经上传过了
            return map;
        }
    }

    //首页走马灯的接口
    @Transactional
    @CrossOrigin
    @GetMapping("/carousel")
    public List<Novel> getcarousel(){
        return novelService.getcarousel();
    }

    //首页推荐的接口(8条信息)
    @Transactional
    @CrossOrigin
    @GetMapping("/recommendPC")
    public List<Novel> getrecommendPC(){
        return novelService.getrecommendPC();
    }

    //首页推荐的接口(6条信息)
    @Transactional
    @CrossOrigin
    @GetMapping("/recommendPhone")
    public List<Novel> getrecommendPhone(){
        return novelService.getrecommendPhone();
    }

    @Transactional
    @CrossOrigin
    @GetMapping("/sort")
    public IPage<Novel> getSortData(@RequestParam("type") String type,@RequestParam("pagination") Integer pagination,@RequestParam("size") Integer size){
        Page<Novel> page = new Page<>(pagination,size);
        QueryWrapper<Novel> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("novel_type",type);
        IPage<Novel> novelIPage = novelService.page(page,queryWrapper);
        return novelIPage;
    }

    @Transactional
    @CrossOrigin
    @PostMapping("/detail/{novelId}")
    public Map<String,Object> getdetail(@RequestHeader String token,@PathVariable("novelId") Long novelId){//登录的用户
        Map<String,Object> map = new HashMap<>();
        QueryWrapper<Novel> queryWrapper1 =new QueryWrapper<>();
        queryWrapper1.eq("novel_id",novelId);
        Novel novel = novelService.getOne(queryWrapper1);
        map.put("novel",novel);
        Long userId;
        try {
            userId = JwtUtil.parseToken(token).getClaim("userId").asLong();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        QueryWrapper<UserAction> queryWrapper2 =new QueryWrapper<>();
        queryWrapper2.eq("novel_id",novelId).eq("user_id",userId);
        UserAction userAction = userActionService.getOne(queryWrapper2);
        map.put("userAction",userAction);

        //从commentAction表中找到用户在该小说中的点赞点踩数据
        QueryWrapper<CommentAction> commentActionQueryWrapper =new QueryWrapper<>();
        commentActionQueryWrapper
                .eq("novel_id",novelId)
                .eq("user_id",userId);
        List<CommentAction> commentActionList = commentActionService.list(commentActionQueryWrapper);
        map.put("commentAction",commentActionList);

        return map;
    }

    @Transactional
    @CrossOrigin
    @PostMapping("/detail_unlisted/{novelId}")
    public Novel getdetail2(@PathVariable("novelId") Long novelId){//未登录的用户
        QueryWrapper<Novel> queryWrapper1 =new QueryWrapper<>();
        queryWrapper1.eq("novel_id",novelId);
        Novel novel = novelService.getOne(queryWrapper1);

        return novel;
    }

    @Transactional
    @CrossOrigin
    @GetMapping("/search")
    public List<Novel> getSearch(@RequestParam("name") String name){
        QueryWrapper<Novel> queryWrapper =new QueryWrapper<>();
        queryWrapper
                .like("novel_name",name)
                .or()
                .like("novel_author",name)
                .or()
                .likeLeft("novel_name",name)
                .or()
                .likeLeft("novel_author",name)
                .or()
                .likeRight("novel_name",name)
                .or()
                .likeRight("novel_author",name);
        return novelService.list(queryWrapper);
    }
}

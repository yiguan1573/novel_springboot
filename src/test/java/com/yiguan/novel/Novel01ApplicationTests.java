package com.yiguan.novel;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiguan.novel.bean.Novel;
import com.yiguan.novel.bean.User;
import com.yiguan.novel.config.PreReadUploadConfig;
import com.yiguan.novel.mapper.UserMapper;
import com.yiguan.novel.service.NovelService;
import com.yiguan.novel.service.UserActionService;
import com.yiguan.novel.service.UserService;
import com.yiguan.novel.utils.FileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class Novel01ApplicationTests {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PreReadUploadConfig uploadConfig;
    @Autowired
    NovelService novelService;
    @Autowired
    UserService userService;
    @Autowired
    UserActionService userActionService;

    @Test
    public void testSelect() {
        //查重
//        QueryWrapper<Novel> queryWrapper = new QueryWrapper<>();
//        queryWrapper
//                .eq("novelName","情与血");
//        Novel novelSelect = novelService.getOne(queryWrapper);
//        System.out.println(novelSelect);

        QueryWrapper<Novel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("novel_name","情与血");
        Novel user2 = novelService.getOne(queryWrapper);
        System.out.println(user2);
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1
                .eq("username","亿观");
        User user1 = userService.getOne(queryWrapper1);
        System.out.println(user1);
    }
    @Test
    public void asda(){
        Date date=new Date();//先获取一个Date对象

        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//创建一个格式化日期对象

        String punchTime = simpleDateFormat.format(date);//格式化后的时间
        System.out.println(punchTime);
    }

    @Test
    public  void FileUpload(){

        System.out.println(FileUtil.deleteImg(uploadConfig.getUploadPath(),"f33fc527f438465995767afc2c86790e.jpg"));
    }
}

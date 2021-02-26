package com.yiguan.novel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiguan.novel.bean.UserAction;
import com.yiguan.novel.mapper.UserActionMapper;
import com.yiguan.novel.service.UserActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserActionServiceImpl  extends ServiceImpl<UserActionMapper,UserAction> implements UserActionService {
    @Autowired
    UserActionMapper userActionMapper;

    @Override
    public Float getAvgScore(Long novelId) {
        return userActionMapper.getAvgScoreByNovelId(novelId);
    }
}

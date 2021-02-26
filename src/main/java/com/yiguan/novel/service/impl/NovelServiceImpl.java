package com.yiguan.novel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiguan.novel.bean.Novel;
import com.yiguan.novel.mapper.NovelMapper;
import com.yiguan.novel.service.NovelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NovelServiceImpl  extends ServiceImpl<NovelMapper, Novel> implements NovelService {
    @Autowired
    NovelMapper novelMapper;

    @Override
    public List<Novel> getcarousel() {
        return novelMapper.getcarousel();
    }

    @Override
    public List<Novel> getrecommendPC() {
        return novelMapper.findByRandPC();
    }

    @Override
    public List<Novel> getrecommendPhone() {
        return novelMapper.findByRandPhone();
    }
}

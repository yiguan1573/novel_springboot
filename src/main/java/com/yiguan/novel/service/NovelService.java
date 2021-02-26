package com.yiguan.novel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yiguan.novel.bean.Novel;

import java.util.List;

public interface NovelService extends IService<Novel> {
    List<Novel> getcarousel();
    List<Novel> getrecommendPC();
    List<Novel> getrecommendPhone();
}

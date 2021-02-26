package com.yiguan.novel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiguan.novel.bean.Novel;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface NovelMapper extends BaseMapper<Novel> {

    @Select("SELECT * FROM novel ORDER BY RAND() LIMIT 0,6")//随机查询6条信息
    List<Novel> getcarousel();

    @Select("SELECT * FROM novel ORDER BY RAND() LIMIT 0,8")//随机查询8条信息
    List<Novel> findByRandPC();
    @Select("SELECT * FROM novel ORDER BY RAND() LIMIT 0,6")//随机查询6条信息
    List<Novel> findByRandPhone();
}


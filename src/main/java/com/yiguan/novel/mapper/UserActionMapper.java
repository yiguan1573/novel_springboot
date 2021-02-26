package com.yiguan.novel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiguan.novel.bean.UserAction;
import org.apache.ibatis.annotations.Select;

public interface UserActionMapper extends BaseMapper<UserAction> {

    @Select("SELECT AVG(user_score) FROM userAction WHERE novel_id = #{novelId} and user_score IS NOT NULL")
    Float getAvgScoreByNovelId(Long novelId);
}

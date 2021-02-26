package com.yiguan.novel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yiguan.novel.bean.UserAction;

public interface UserActionService extends IService<UserAction> {
    Float getAvgScore(Long novelId);
}

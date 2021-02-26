package com.yiguan.novel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiguan.novel.bean.User;
import com.yiguan.novel.mapper.UserMapper;
import com.yiguan.novel.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

}

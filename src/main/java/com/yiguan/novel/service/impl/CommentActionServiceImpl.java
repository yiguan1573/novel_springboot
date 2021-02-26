package com.yiguan.novel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiguan.novel.bean.CommentAction;
import com.yiguan.novel.mapper.CommentActionMapper;
import com.yiguan.novel.service.CommentActionService;
import org.springframework.stereotype.Service;

@Service
public class CommentActionServiceImpl extends ServiceImpl<CommentActionMapper, CommentAction> implements CommentActionService {
}

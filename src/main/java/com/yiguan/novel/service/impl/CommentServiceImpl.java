package com.yiguan.novel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yiguan.novel.bean.Comment;
import com.yiguan.novel.mapper.CommentMapper;
import com.yiguan.novel.service.CommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}

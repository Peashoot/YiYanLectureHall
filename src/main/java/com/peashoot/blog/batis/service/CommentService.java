package com.peashoot.blog.batis.service;

import com.peashoot.blog.batis.entity.Comment;
import com.peashoot.blog.batis.service.base.InsertService;
import com.peashoot.blog.batis.service.base.RemoveService;
import com.peashoot.blog.batis.service.base.SelectService;

public interface CommentService extends InsertService<Comment>,
        SelectService<Comment, Integer>, RemoveService<Comment, Integer> {
}

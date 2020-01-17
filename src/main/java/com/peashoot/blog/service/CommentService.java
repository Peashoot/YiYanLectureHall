package com.peashoot.blog.service;

import com.peashoot.blog.entity.Comment;
import com.peashoot.blog.service.base.InsertService;
import com.peashoot.blog.service.base.RemoveService;
import com.peashoot.blog.service.base.SelectService;

public interface CommentService extends InsertService<Comment>,
        SelectService<Comment, Integer>, RemoveService<Comment, Integer> {
}

package com.peashoot.blog.batis.service;

import com.peashoot.blog.batis.entity.Visitor;
import com.peashoot.blog.batis.service.base.InsertService;
import com.peashoot.blog.batis.service.base.SelectService;

public interface VisitorService extends InsertService<Visitor>, SelectService<Visitor, Long> {
}

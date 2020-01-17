package com.peashoot.blog.service;

import com.peashoot.blog.entity.Visitor;
import com.peashoot.blog.service.base.InsertService;
import com.peashoot.blog.service.base.SelectService;

public interface VisitorService extends InsertService<Visitor>, SelectService<Visitor, Long> {
}

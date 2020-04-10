package com.peashoot.blog.batis.service;

import com.peashoot.blog.batis.entity.FileDO;
import com.peashoot.blog.batis.service.base.InsertService;
import com.peashoot.blog.batis.service.base.SelectService;

public interface FileService extends InsertService<FileDO>, SelectService<FileDO, String> {
}

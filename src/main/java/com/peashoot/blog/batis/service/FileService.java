package com.peashoot.blog.batis.service;

import com.peashoot.blog.batis.entity.FileDo;
import com.peashoot.blog.batis.service.base.InsertService;
import com.peashoot.blog.batis.service.base.SelectService;

public interface FileService extends InsertService<FileDo>, SelectService<FileDo, String> {
}

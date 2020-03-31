package com.peashoot.blog.batis.service.impl;

import com.peashoot.blog.batis.entity.FileDo;
import com.peashoot.blog.batis.service.FileService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public int insert(FileDo insertItem) {
        return 0;
    }

    @Override
    public List<FileDo> selectAll() {
        return null;
    }

    @Override
    public FileDo selectById(String id) {
        return null;
    }
}

package com.peashoot.blog.batis.service.impl;

import com.peashoot.blog.batis.entity.FileDO;
import com.peashoot.blog.batis.mapper.FileMapper;
import com.peashoot.blog.batis.service.FileService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    private final FileMapper fileMapper;

    public FileServiceImpl(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @Override
    public int insert(FileDO insertItem) {
        return fileMapper.insert(insertItem);
    }

    @Override
    public List<FileDO> selectAll() {
        return fileMapper.selectAll();
    }

    @Override
    public FileDO selectById(String id) {
        return fileMapper.selectByPrimaryKey(id);
    }
}

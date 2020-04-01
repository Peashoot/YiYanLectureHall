package com.peashoot.blog.batis.service.impl;

import com.peashoot.blog.batis.entity.FileDo;
import com.peashoot.blog.batis.mapper.FileMapper;
import com.peashoot.blog.batis.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    private final FileMapper fileMapper;

    public FileServiceImpl(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @Override
    public int insert(FileDo insertItem) {
        return fileMapper.insert(insertItem);
    }

    @Override
    public List<FileDo> selectAll() {
        return fileMapper.selectAll();
    }

    @Override
    public FileDo selectById(String id) {
        return fileMapper.selectById(id);
    }
}

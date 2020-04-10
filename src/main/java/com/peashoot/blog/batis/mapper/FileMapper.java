package com.peashoot.blog.batis.mapper;

import com.peashoot.blog.batis.entity.FileDO;
import com.peashoot.blog.batis.mapper.base.InsertMapper;
import com.peashoot.blog.batis.mapper.base.SelectMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMapper extends InsertMapper<FileDO>, SelectMapper<FileDO, String> {
}

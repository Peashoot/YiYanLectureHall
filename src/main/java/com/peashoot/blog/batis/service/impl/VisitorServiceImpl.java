package com.peashoot.blog.batis.service.impl;

import com.peashoot.blog.batis.entity.VisitorDO;
import com.peashoot.blog.batis.mapper.SysUserMapper;
import com.peashoot.blog.batis.mapper.VisitorMapper;
import com.peashoot.blog.batis.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.peashoot.blog.batis.service.VisitorService;

import java.util.List;

@Service("visitorService")
public class VisitorServiceImpl implements VisitorService {
    private final VisitorMapper visitorMapper;

    public VisitorServiceImpl(VisitorMapper visitorMapper) {
        this.visitorMapper = visitorMapper;
    }

    @Override
    public int insert(VisitorDO insertItem) {
        return visitorMapper.insert(insertItem);
    }

    @Override
    public long insertWithReturnRecordId(VisitorDO insertItem) {
        return visitorMapper.insertWithReturnRecordID(insertItem);
    }

    @Override
    public VisitorDO selectByVisitorName(String visitorName) {
        return visitorMapper.selectByVisitorName(visitorName);
    }

    @Override
    public List<VisitorDO> selectAll() {
        return visitorMapper.selectAll();
    }

    @Override
    public VisitorDO selectById(Long id) {
        return visitorMapper.selectByPrimaryKey(id);
    }

    @Override
    public VisitorDO selectByIpAndBrowser(String visitIp, String browser) {
        return visitorMapper.selectByIPAndBrowser(visitIp, browser);
    }

    @Override
    public int update(VisitorDO updateItem) {
        return visitorMapper.updateByPrimaryKey(updateItem);
    }
}

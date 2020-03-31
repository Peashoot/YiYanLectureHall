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
    @Autowired
    private VisitorMapper visitorMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public int insert(VisitorDO insertItem) {
        return visitorMapper.insert(insertItem);
    }

    @Override
    public long insertWithReturnRecordID(VisitorDO insertItem) {
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
    public VisitorDO selectByIPAndBrowser(String visitIP, String browser) {
        return visitorMapper.selectByIPAndBrowser(visitIP, browser);
    }

    @Override
    public int update(VisitorDO updateItem) {
        return visitorMapper.updateByPrimaryKey(updateItem);
    }
}

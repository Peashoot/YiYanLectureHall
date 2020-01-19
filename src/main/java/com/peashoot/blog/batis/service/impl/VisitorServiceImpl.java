package com.peashoot.blog.batis.service.impl;

import com.peashoot.blog.batis.entity.Visitor;
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
    public int insert(Visitor insertItem) {
        if (insertItem.getRole() != null) {
            insertItem.setRoleId(insertItem.getRole().getId());
        }
        if (insertItem.getUser() != null) {
            insertItem.setUserId(insertItem.getUser().getId());
        }
        return visitorMapper.insert(insertItem);
    }

    @Override
    public List<Visitor> selectAll() {
        List<Visitor> retList = visitorMapper.selectAll();
        for (Visitor v : retList) {
            v.setRole(roleMapper.selectByPrimaryKey(v.getRoleId()));
            v.setUser(sysUserMapper.selectByPrimaryKey(v.getUserId()));
        }
        return retList;
    }

    @Override
    public Visitor selectById(Long id) {
        Visitor retObj = visitorMapper.selectByPrimaryKey(id);
        retObj.setRole(roleMapper.selectByPrimaryKey(retObj.getRoleId()));
        retObj.setUser(sysUserMapper.selectByPrimaryKey(retObj.getUserId()));
        return retObj;
    }
}

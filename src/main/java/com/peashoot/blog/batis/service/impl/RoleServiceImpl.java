package com.peashoot.blog.batis.service.impl;

import com.peashoot.blog.batis.entity.RoleDO;
import com.peashoot.blog.batis.mapper.RoleMapper;
import com.peashoot.blog.batis.mapper.SysUserMapper;
import com.peashoot.blog.batis.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("roleService")
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private SysUserMapper sysuserMapper;
    @Override
    public int insert(RoleDO insertItem) {
        if (insertItem.getInsertUser() != null) {
            insertItem.setInsertUserId(insertItem.getInsertUser().getId());
            insertItem.setUpdateUserId(insertItem.getInsertUser().getId());
        }
        insertItem.setUpdateTime(insertItem.getInsertTime());
        return roleMapper.insert(insertItem);
    }

    @Override
    public int remove(Integer removeId) {
        return roleMapper.deleteByPrimaryKey(removeId);
    }

    @Override
    public int removeRange(List<Integer> removeIdList) {
        return roleMapper.deleteRangeByPrimaryKeys(removeIdList.toArray(new Integer[0]));
    }

    @Override
    public List<RoleDO> selectAll() {
        List<RoleDO> retList = roleMapper.selectAll();
        for (RoleDO role : retList) {
            role.setInsertUser(sysuserMapper.selectByPrimaryKey(role.getInsertUserId()));
            role.setUpdateUser(sysuserMapper.selectByPrimaryKey(role.getUpdateUserId()));
        }
        return retList;
    }

    @Override
    public RoleDO selectById(Integer id) {
        RoleDO role = roleMapper.selectByPrimaryKey(id);
        role.setInsertUser(sysuserMapper.selectByPrimaryKey(role.getInsertUserId()));
        role.setUpdateUser(sysuserMapper.selectByPrimaryKey(role.getUpdateUserId()));
        return role;
    }

    @Override
    public int update(RoleDO updateItem) {
        if (updateItem.getInsertUser() != null) {
            updateItem.setInsertUserId(updateItem.getInsertUser().getId());
        }
        if (updateItem.getUpdateUser() != null) {
            updateItem.setUpdateUserId(updateItem.getUpdateUser().getId());
        }
        return roleMapper.updateByPrimaryKey(updateItem);
    }
}

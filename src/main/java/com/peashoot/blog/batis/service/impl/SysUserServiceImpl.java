package com.peashoot.blog.batis.service.impl;

import com.peashoot.blog.batis.mapper.SysUserMapper;
import com.peashoot.blog.batis.entity.RoleDO;
import com.peashoot.blog.batis.entity.SysUserDO;
import com.peashoot.blog.batis.mapper.RoleMapper;
import com.peashoot.blog.batis.service.SysUserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {
    private final SysUserMapper sysuserMapper;

    public SysUserServiceImpl(SysUserMapper sysuserMapper, RoleMapper roleMapper) {
        this.sysuserMapper = sysuserMapper;
    }

    @Override
    public int insert(SysUserDO insertItem) {
        insertItem.concatRoleIds();
        insertItem.setUpdateTime(insertItem.getRegisterTime());
        insertItem.setLastPasswordResetTime(insertItem.getRegisterTime());
        return sysuserMapper.insert(insertItem);
    }

    @Override
    public int remove(Integer removeId) {
        return sysuserMapper.deleteByPrimaryKey(removeId);
    }

    @Override
    public int removeRange(@NotNull List<Integer> removeIdList) {
        return sysuserMapper.deleteRangeByPrimaryKeys(removeIdList.toArray(new Integer[0]));
    }

    @Override
    public List<SysUserDO> selectAll() {
        return sysuserMapper.selectAll();
    }

    @Override
    public SysUserDO selectById(Integer id) {
        return sysuserMapper.selectWithRolesById(id);
    }

    @Override
    public int update(SysUserDO updateItem) {
        updateItem.concatRoleIds();
        return sysuserMapper.updateByPrimaryKey(updateItem);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserDO user = sysuserMapper.selectWithRoleByUsernameOrEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Check username and password.");
        }
        return user;
    }

    @Override
    public int getIdByUsername(String username) {
        return sysuserMapper.getIdByUsername(username);
    }

    @Override
    public boolean lockUser(String username, Date lockDate) {
        return sysuserMapper.lockUser(username, lockDate) > 0;
    }
}

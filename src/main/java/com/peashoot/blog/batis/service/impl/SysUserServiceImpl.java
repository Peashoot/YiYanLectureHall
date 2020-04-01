package com.peashoot.blog.batis.service.impl;

import com.peashoot.blog.batis.mapper.SysUserMapper;
import com.peashoot.blog.batis.entity.RoleDO;
import com.peashoot.blog.batis.entity.SysUserDO;
import com.peashoot.blog.batis.mapper.RoleMapper;
import com.peashoot.blog.batis.service.SysUserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {
    private final SysUserMapper sysuserMapper;
    private final RoleMapper roleMapper;

    public SysUserServiceImpl(SysUserMapper sysuserMapper, RoleMapper roleMapper) {
        this.sysuserMapper = sysuserMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public int insert(SysUserDO insertItem) {
        combineRoleIds(insertItem);
        insertItem.setUpdateTime(insertItem.getRegisterTime());
        insertItem.setLastPasswordResetDate(insertItem.getRegisterTime());
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
        List<SysUserDO> retList = sysuserMapper.selectAll();
        for (SysUserDO sysUser : retList) {
            resolveIdToRoles(sysUser);
        }
        return retList;
    }

    @Override
    public SysUserDO selectById(Integer id) {
        return sysuserMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(SysUserDO updateItem) {
        combineRoleIds(updateItem);
        return sysuserMapper.updateByPrimaryKey(updateItem);
    }

    /**
     * 将用户的角色类型id转成对应的角色列表
     *
     * @param user 用户
     */
    private void resolveIdToRoles(SysUserDO user) {
        if (user == null || user.getRoleIds() == null || user.getRoleIds().isEmpty()) {
            return;
        }
        String[] splitIds = user.getRoleIds().split(",");
        List<RoleDO> roleList = new ArrayList<>();
        for (String strId : splitIds) {
            roleList.add(roleMapper.selectByPrimaryKey(Integer.parseInt(strId)));
        }
        user.setRoles(roleList);
    }

    /**
     * 将用户的角色列表转成角色类型id
     *
     * @param user 用户
     */
    private void combineRoleIds(SysUserDO user) {
        if (user == null || user.getRoles() == null || user.getRoles().size() < 1) {
            return;
        }
        StringBuilder roleIds = new StringBuilder();
        for (RoleDO role : user.getRoles()) {
            roleIds.append(role.getId()).append(',');
        }
        roleIds.deleteCharAt(roleIds.length() - 1);
        user.setRoleIds(roleIds.toString());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserDO user = sysuserMapper.selectByUsernameOrEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Check username and password.");
        }
        resolveIdToRoles(user);
        return user;
    }

    @Override
    public int getIdByUsername(String username) {
        return sysuserMapper.getIdByUsername(username);
    }
}

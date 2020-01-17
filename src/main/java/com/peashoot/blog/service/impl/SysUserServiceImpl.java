package com.peashoot.blog.service.impl;

import com.peashoot.blog.common.JwtTokenUtil;
import com.peashoot.blog.entity.Role;
import com.peashoot.blog.entity.SysUser;
import com.peashoot.blog.mapper.RoleMapper;
import com.peashoot.blog.mapper.SysUserMapper;
import com.peashoot.blog.service.SysUserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysuserMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public int insert(SysUser insertItem) {
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
    public List<SysUser> selectAll() {
        List<SysUser> retList = sysuserMapper.selectAll();
        for (SysUser sysUser : retList) {
            resolveIdToRoles(sysUser);
        }
        return retList;
    }

    @Override
    public SysUser selectById(Integer id) {
        SysUser sysUser = sysuserMapper.selectByPrimaryKey(id);
        return sysUser;
    }

    @Override
    public int update(SysUser updateItem) {
        combineRoleIds(updateItem);
        return sysuserMapper.updateByPrimaryKey(updateItem);
    }

    /**
     * 将用户的角色类型id转成对应的角色列表
     *
     * @param user 用户
     */
    private void resolveIdToRoles(SysUser user) {
        if (user == null || user.getRoleIds() == null || user.getRoleIds().isEmpty()) {
            return;
        }
        String[] splitIds = user.getRoleIds().split(",");
        List<Role> roleList = new ArrayList<>();
        for (String strId : splitIds) {
            roleList.add(roleMapper.selectByPrimaryKey(Integer.parseInt(strId)));
        }
        user.setRoles(roleList);
        return;
    }

    /**
     * 将用户的角色列表转成角色类型id
     *
     * @param user 用户
     */
    private void combineRoleIds(SysUser user) {
        if (user == null || user.getRoles() == null || user.getRoles().size() < 1) {
            return;
        }
        StringBuilder roleIds = new StringBuilder();
        for (Role role : user.getRoles()) {
            roleIds.append(role.getId()).append(',');
        }
        roleIds.deleteCharAt(roleIds.length() - 1);
        user.setRoleIds(roleIds.toString());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysuserMapper.selectByUsernameOrEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Check username and password.");
        }
        resolveIdToRoles(user);
        return user;
    }
}

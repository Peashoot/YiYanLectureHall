package com.peashoot.blog.batis.service;

import com.peashoot.blog.batis.service.base.InsertService;
import com.peashoot.blog.batis.service.base.RemoveService;
import com.peashoot.blog.batis.service.base.SelectService;
import com.peashoot.blog.batis.service.base.UpdateService;
import com.peashoot.blog.batis.entity.SysUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface SysUserService extends InsertService<SysUser>, UpdateService<SysUser>,
        RemoveService<SysUser, Integer>, SelectService<SysUser, Integer>, UserDetailsService {
    int getIdByUsername(String username);
}

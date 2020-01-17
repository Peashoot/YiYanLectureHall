package com.peashoot.blog.service;

import com.peashoot.blog.service.base.InsertService;
import com.peashoot.blog.service.base.RemoveService;
import com.peashoot.blog.service.base.SelectService;
import com.peashoot.blog.service.base.UpdateService;
import com.peashoot.blog.entity.SysUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface SysUserService extends InsertService<SysUser>, UpdateService<SysUser>,
        RemoveService<SysUser, Integer>, SelectService<SysUser, Integer>, UserDetailsService {
}

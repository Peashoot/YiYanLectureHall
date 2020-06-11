package com.peashoot.blog.batis.service;

import com.peashoot.blog.batis.service.base.InsertService;
import com.peashoot.blog.batis.service.base.RemoveService;
import com.peashoot.blog.batis.service.base.SelectService;
import com.peashoot.blog.batis.service.base.UpdateService;
import com.peashoot.blog.batis.entity.SysUserDO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Date;

public interface SysUserService extends InsertService<SysUserDO>, UpdateService<SysUserDO>,
        RemoveService<SysUserDO, Integer>, SelectService<SysUserDO, Integer>, UserDetailsService {
    /**
     * 根据用户名获取id
     *
     * @param username 用户账号
     * @return id
     */
    int getIdByUsername(String username);

    /**
     * 锁定用户
     * @param username 用户名
     * @param lockDate 锁定日期
     * @return 是否成功
     */
    boolean lockUser(String username, Date lockDate);
}

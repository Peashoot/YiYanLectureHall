package com.peashoot.blog.batis.service;

import com.peashoot.blog.batis.entity.RoleDO;
import com.peashoot.blog.batis.service.base.InsertService;
import com.peashoot.blog.batis.service.base.RemoveService;
import com.peashoot.blog.batis.service.base.SelectService;
import com.peashoot.blog.batis.service.base.UpdateService;

public interface RoleService extends InsertService<RoleDO>, UpdateService<RoleDO>,
        RemoveService<RoleDO, Integer>, SelectService<RoleDO, Integer> {
    public RoleDO selectByRoleName(String roleName);
}

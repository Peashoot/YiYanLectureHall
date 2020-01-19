package com.peashoot.blog.batis.service;

import com.peashoot.blog.batis.entity.Role;
import com.peashoot.blog.batis.service.base.InsertService;
import com.peashoot.blog.batis.service.base.RemoveService;
import com.peashoot.blog.batis.service.base.SelectService;
import com.peashoot.blog.batis.service.base.UpdateService;

public interface RoleService extends InsertService<Role>, UpdateService<Role>,
        RemoveService<Role, Integer>, SelectService<Role, Integer> {
}

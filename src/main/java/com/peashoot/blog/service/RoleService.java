package com.peashoot.blog.service;

import com.peashoot.blog.entity.Role;
import com.peashoot.blog.service.base.InsertService;
import com.peashoot.blog.service.base.RemoveService;
import com.peashoot.blog.service.base.SelectService;
import com.peashoot.blog.service.base.UpdateService;

public interface RoleService extends InsertService<Role>, UpdateService<Role>,
        RemoveService<Role, Integer>, SelectService<Role, Integer> {
}

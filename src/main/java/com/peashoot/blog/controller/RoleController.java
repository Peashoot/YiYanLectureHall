package com.peashoot.blog.controller;

import com.peashoot.blog.aspect.annotation.ErrorRecord;
import com.peashoot.blog.batis.entity.RoleDO;
import com.peashoot.blog.batis.entity.SysUserDO;
import com.peashoot.blog.batis.enums.PermissionEnum;
import com.peashoot.blog.batis.service.RoleService;
import com.peashoot.blog.context.request.role.ChangedRoleDTO;
import com.peashoot.blog.context.response.ApiResp;
import com.peashoot.blog.exception.UserNotLoginException;
import com.peashoot.blog.util.SecurityUtil;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("role")
@EnableAsync
@ErrorRecord
public class RoleController {
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 角色操作类
     */
    private RoleService roleService;

    @PostMapping("modify")
    @PreAuthorize("hasAuthority('role_modify')")
    public ApiResp<Boolean> modifyRole(@RequestBody ChangedRoleDTO apiReq) throws UserNotLoginException {
        SysUserDO sysUser = SecurityUtil.getCurrentUser();
        if (sysUser == null) {
            throw new UserNotLoginException();
        }
        RoleDO roleDo, existRoleDo = roleService.selectByRoleName(apiReq.getRoleName());
        boolean updateMode = false;
        if (Optional.ofNullable(apiReq.getRoleId()).orElse(0) > 0) {
            roleDo = roleService.selectById(apiReq.getRoleId());
            updateMode = true;
            roleDo.setUpdateTime(new Date());
            roleDo.setUpdateUser(sysUser);
        } else {
            roleDo = new RoleDO();
            roleDo.setInsertUser(sysUser);
            roleDo.setInsertTime(new Date());
            roleDo.setUpdateUser(sysUser);
            roleDo.setUpdateTime(new Date());
        }
        if (existRoleDo != null && !Objects.equals(existRoleDo.getId(), roleDo.getId())) {
            return new ApiResp<>(501, "Exist the same role");
        }
        roleDo.setPermissions(apiReq.getPermissions());
        roleDo.setRoleName(apiReq.getRoleName());
        boolean success = (updateMode ? roleService.update(roleDo) : roleService.insert(roleDo)) > 0;
        return new ApiResp<Boolean>().success().fill(success);
    }
}

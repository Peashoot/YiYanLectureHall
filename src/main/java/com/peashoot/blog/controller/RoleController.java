package com.peashoot.blog.controller;

import com.peashoot.blog.aspect.annotation.ErrorRecord;
import com.peashoot.blog.batis.entity.RoleDO;
import com.peashoot.blog.batis.entity.SysUserDO;
import com.peashoot.blog.batis.enums.VisitActionEnum;
import com.peashoot.blog.batis.service.OperateRecordService;
import com.peashoot.blog.batis.service.RoleService;
import com.peashoot.blog.context.request.role.ChangedRoleDTO;
import com.peashoot.blog.context.response.ApiResp;
import com.peashoot.blog.exception.UserNotLoginException;
import com.peashoot.blog.util.IpUtils;
import com.peashoot.blog.util.SecurityUtil;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("role")
@EnableAsync
@ErrorRecord
public class RoleController {
    public RoleController(RoleService roleService, OperateRecordService operateRecordService) {
        this.roleService = roleService;
        this.operateRecordService = operateRecordService;
    }

    /**
     * 角色操作类
     */
    private RoleService roleService;
    /**
     * 操作记录操作类
     */
    private OperateRecordService operateRecordService;

    /**
     * 新增或修改角色
     * @param apiReq 请求
     * @return 修改结果
     * @throws UserNotLoginException 用户未登录
     */
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
        operateRecordService.insertNewRecordAsync(sysUser.getId(), roleDo.getId().toString(), apiReq.getVisitorIp(),
                roleDo.getId() > 0 ? VisitActionEnum.ROLE_UPDATE : VisitActionEnum.ROLE_CREATE, new Date(),
                (roleDo.getId() > 0 ? "Update" : "Create") + " an role " + roleDo.getRoleName());
        boolean success = (updateMode ? roleService.update(roleDo) : roleService.insert(roleDo)) > 0;
        return new ApiResp<Boolean>().success().fill(success);
    }

    /**
     * 删除角色
     * @param roleId 角色id
     * @return 删除结果
     * @throws UserNotLoginException 用户未登录异常
     */
    @PostMapping("remove")
    @PreAuthorize("hasAuthority('role_remove')")
    public ApiResp<Boolean> removeRole(HttpServletRequest request, @RequestParam Integer roleId) throws UserNotLoginException  {
        SysUserDO sysUser = SecurityUtil.getCurrentUser();
        if (sysUser == null) {
            throw new UserNotLoginException();
        }
        ApiResp<Boolean> retResp = new ApiResp<>();
        RoleDO roleDO = roleService.selectById(roleId);
        if (roleDO == null) {
            retResp.setCode(ApiResp.NO_RECORD_MATCH);
            retResp.setMessage("No match record.");
            return retResp;
        }
        operateRecordService.insertNewRecordAsync(sysUser.getId(), Optional.ofNullable(roleId).orElse(0).toString(), IpUtils.getIpAddr(request),
                VisitActionEnum.ROLE_REMOVE, new Date(),"Remove an role" + roleDO.getRoleName());
        if (roleService.remove(roleId) == 0) {
            retResp.setCode(ApiResp.PROCESS_ERROR);
            retResp.setMessage("Failure to remove.");
            return retResp;
        }
        retResp.success().setData(true);
        return retResp;
    }
}

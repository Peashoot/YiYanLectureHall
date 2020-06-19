package com.peashoot.blog.controller;

import com.google.common.collect.ImmutableList;
import com.peashoot.blog.aspect.annotation.ErrorRecord;
import com.peashoot.blog.aspect.annotation.VisitTimesLimit;
import com.peashoot.blog.batis.entity.VisitorDO;
import com.peashoot.blog.batis.enums.VisitActionEnum;
import com.peashoot.blog.batis.service.*;
import com.peashoot.blog.context.request.sysuser.*;
import com.peashoot.blog.crypto.annotation.DecryptRequest;
import com.peashoot.blog.batis.entity.RoleDO;
import com.peashoot.blog.batis.entity.SysUserDO;
import com.peashoot.blog.context.response.ApiResp;
import com.peashoot.blog.exception.UserNameOccupiedException;
import com.peashoot.blog.exception.UserUnmatchedException;
import com.peashoot.blog.util.IpUtils;
import com.peashoot.blog.util.SecurityUtil;
import com.peashoot.blog.util.modules.Lazy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * 用户管理相关接口
 *
 * @author peashoot
 */
@RestController
@Api(tags = "用户管理相关接口")
@RequestMapping(path = "user")
@ErrorRecord
public class SysUserController {
    /**
     * 用户信息操作类
     */
    private final SysUserService sysUserService;
    /**
     * 用户认证操作类
     */
    private final AuthService authService;
    /**
     * 用户操作记录
     */
    private final OperateRecordService operateRecordService;
    /**
     * 访客信息操作类
     */
    private final VisitorService visitorService;
    /**
     * 普通用户角色信息
     */
    private Lazy<RoleDO> roleNormalUserLazy;

    public SysUserController(SysUserService sysUserService, AuthService authService, OperateRecordService operateRecordService,
                             RoleService roleService, VisitorService visitorService) {
        this.sysUserService = sysUserService;
        this.authService = authService;
        this.operateRecordService = operateRecordService;
        this.visitorService = visitorService;
        roleNormalUserLazy = new Lazy<>(() -> roleService.selectByRoleName(RoleDO.ROLE_NORMAL_USER));
    }

    /**
     * 根据用户名密码进行登录
     *
     * @param apiReq 用户账户信息
     * @return 带有token的报文
     */
    @PostMapping(path = "login")
    @ApiOperation("根据用户名或者邮箱进行登录")
    @DecryptRequest
    @VisitTimesLimit
    public ApiResp<String> loginByUserNameAndPassword(@RequestBody @Validated LoginUserDTO apiReq) {
        ApiResp<String> resp = new ApiResp<>();
        int userId = sysUserService.getIdByUsername(apiReq.getUsername());
        operateRecordService.insertNewRecordAsync(userId, String.valueOf(userId), apiReq.getVisitorIp(), VisitActionEnum.USER_LOGIN, new Date(),
                "User " + apiReq.getUsername() + " try to login in.");
        String token = authService.login(apiReq.getUsername(), apiReq.getPassword(), apiReq.getVisitorIp(), apiReq.getBrowserFingerprint());
        resp.success().setData(token);
        resp.setTimestamp(System.currentTimeMillis());
        return resp;
    }

    /**
     * 注册系统用户
     *
     * @param apiReq 注册信息
     * @return 返回是否注册成功
     */
    @PostMapping(path = "register")
    @ApiOperation("注册用户")
    @DecryptRequest
    public ApiResp<Boolean> registerSysUser(@RequestBody @Validated RegisterUserDTO apiReq) {
        ApiResp<Boolean> resp = new ApiResp<>();
        VisitorDO visitorDO = visitorService.selectByVisitorName(apiReq.getVisitor());
        if (visitorDO == null) {
            resp.setCode(ApiResp.NO_VISITOR_MATCH);
            resp.setMessage("No matched visitor");
            return resp;
        }
        SysUserDO sysUser = new SysUserDO();
        apiReq.copyTo(sysUser);
        sysUser.setUsername(apiReq.getUsername());
        sysUser.setEmail(apiReq.getEmail());
        sysUser.setPassword(apiReq.getPassword());
        String salt = UUID.randomUUID().toString().replace("-", "");
        sysUser.initialize(new Date(), ImmutableList.of(roleNormalUserLazy.getInstance()), salt);
        try {
            operateRecordService.insertNewRecordAsync(visitorDO.getId(), apiReq.getUsername(), apiReq.getVisitorIp(), VisitActionEnum.USER_REGISTER, new Date(),
                    "Visitor try to register " + apiReq.getUsername() + ".");
            boolean success = authService.insertSysUser(sysUser);
            resp.success().setData(success);
        } catch (UserNameOccupiedException ex) {
            resp.setCode(ApiResp.BAD_REQUEST);
            resp.setMessage("Failure to register.");
            resp.setData(false);
        }
        return resp;
    }

    /**
     * 修改用户密码
     *
     * @param apiReq 修改密码
     * @return 修改结果
     */
    @PostMapping(path = "change/pwd")
    @ApiOperation("修改用户密码")
    @PreAuthorize("hasAuthority('change_pwd')")
    public ApiResp<Boolean> changePassword(@RequestBody @Validated ChangePwdDTO apiReq) {
        SysUserDO curUser = SecurityUtil.getCurrentUser();
        if (curUser == null || !curUser.getUsername().equals(apiReq.getUsername())) {
            throw new UserUnmatchedException();
        }
        int userId = sysUserService.getIdByUsername(apiReq.getUsername());
        operateRecordService.insertNewRecordAsync(userId, String.valueOf(userId), apiReq.getVisitorIp(), VisitActionEnum.USER_CHANGE_PASSWORD, new Date(),
                "User " + apiReq.getUsername() + " try to change password.");
        boolean success = authService.changePassword(apiReq.getUsername(), apiReq.getOldPassword(), apiReq.getNewPassword());
        ApiResp<Boolean> resp = new ApiResp<>();
        resp.success().setData(success);
        return resp;
    }

    /**
     * 修改用户信息
     *
     * @param apiReq 修改后的用户信息
     * @return 是否修改成功
     */
    @PostMapping(path = "change/other")
    @ApiOperation("修改用户信息")
    @PreAuthorize("hasAuthority('change_detail')")
    public ApiResp<Boolean> changeUserInfo(@RequestBody ChangeDetailDTO apiReq) {
        SysUserDO curUser = SecurityUtil.getCurrentUser();
        if (curUser == null || !curUser.getId().equals(apiReq.getId())) {
            throw new UserUnmatchedException();
        }
        SysUserDO sysUser = sysUserService.selectById(apiReq.getId());
        ApiResp<Boolean> resp = new ApiResp<>();
        if (sysUser == null) {
            resp.setCode(ApiResp.NO_RECORD_MATCH);
            resp.setMessage("Please check your account carefully.");
            resp.setData(false);
            return resp;
        }
        apiReq.copyTo(sysUser);
        operateRecordService.insertNewRecordAsync(sysUser.getId(), sysUser.getId().toString(), apiReq.getVisitorIp(), VisitActionEnum.USER_CHANGE_INFORMATION, new Date(),
                "User " + sysUser.getUsername() + " try to change information.");
        boolean result = sysUserService.update(sysUser) > 0;
        if (result) {
            resp.success().setData(true);
        } else {
            resp.setCode(ApiResp.PROCESS_ERROR);
            resp.setMessage("Failure to change information.");
            resp.setData(false);
        }
        return resp;
    }

    @RequestMapping(path = "logout")
    @ApiOperation("用户登出")
    @PreAuthorize("hasAuthority('logout')")
    public ApiResp<Boolean> logOut(HttpServletRequest request, @RequestParam String username) {
        ApiResp<Boolean> resp = new ApiResp<Boolean>().success();
        int userId = sysUserService.getIdByUsername(username);
        operateRecordService.insertNewRecordAsync(userId, String.valueOf(userId), IpUtils.getIpAddr(request), VisitActionEnum.USER_LOGOUT, new Date(),
                "User " + username + " try to log out.");
        resp.setData(authService.logOut(username));
        return resp;
    }

    @RequestMapping(path = "apply/resetPwd")
    @ApiOperation("申请重置密码")
    public ApiResp<Boolean> applyResetPassword(HttpServletRequest request, @RequestParam @NotNull @Validated String username) {
        ApiResp<Boolean> resp = new ApiResp<>();
        SysUserDO user = (SysUserDO) sysUserService.loadUserByUsername(username);
        if (user == null) {
            resp.setCode(ApiResp.NO_RECORD_MATCH);
            resp.setMessage("Please check you account carefully.");
            return resp;
        }
        if (!sysUserService.lockUser(username, new Date())) {
            resp.setCode(ApiResp.PROCESS_ERROR);
            resp.setMessage("Failure to lock account.");
            return resp;
        }
        operateRecordService.insertNewRecordAsync(user.getId(), user.getId().toString(), IpUtils.getIpAddr(request), VisitActionEnum.USER_APPLY_RETRIEVE_PASSWORD, new Date(),
                "User " + username + " apply to change password.");
        if (!authService.sendResetPasswordEmail(user)) {
            resp.setCode(ApiResp.PROCESS_ERROR);
            resp.setMessage("Failure to send reset email");
            return resp;
        }
        // 发送邮件到用户注册邮箱
        resp.success().setData(true);
        return resp;
    }

    @RequestMapping(path = "resetPwd")
    @ApiOperation("重置密码")
    @PreAuthorize("hasAuthority('reset_pwd')")
    public ApiResp<Boolean> resetPassword(@RequestParam("applyId") String applySerial, @RequestBody ChangePwdDTO changePwd) {
        int userId = sysUserService.getIdByUsername(changePwd.getUsername());
        operateRecordService.insertNewRecordAsync(userId, String.valueOf(userId), changePwd.getVisitorIp(), VisitActionEnum.USER_RETRIEVE_PASSWORD, new Date(),
                "User " + changePwd.getUsername() + " try to retrieve password.");
        ApiResp<Boolean> resp = new ApiResp<>();
        if (!authService.resetPassword(changePwd.getUsername(), applySerial, changePwd.getNewPassword())) {
            resp.setCode(ApiResp.PROCESS_ERROR);
            resp.setMessage("Failure to reset password");
            return resp;
        }
        resp.success().setData(true);
        return resp;
    }
}

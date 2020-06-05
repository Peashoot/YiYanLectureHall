package com.peashoot.blog.controller;

import com.peashoot.blog.aspect.annotation.ErrorRecord;
import com.peashoot.blog.aspect.annotation.VisitLimit;
import com.peashoot.blog.batis.enums.VisitActionEnum;
import com.peashoot.blog.batis.service.OperateRecordService;
import com.peashoot.blog.context.request.sysuser.*;
import com.peashoot.blog.crypto.annotation.DecryptRequest;
import com.peashoot.blog.batis.entity.RoleDO;
import com.peashoot.blog.batis.entity.SysUserDO;
import com.peashoot.blog.context.response.ApiResp;
import com.peashoot.blog.batis.service.AuthService;
import com.peashoot.blog.batis.service.SysUserService;
import com.peashoot.blog.exception.UserNameOccupiedException;
import com.peashoot.blog.exception.UserUnmatchedException;
import com.peashoot.blog.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping(path = "system")
@EnableAsync
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

    public SysUserController(SysUserService sysUserService, AuthService authService, OperateRecordService operateRecordService) {
        this.sysUserService = sysUserService;
        this.authService = authService;
        this.operateRecordService = operateRecordService;
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
    @VisitLimit
    public ApiResp<String> loginByUserNameAndPassword(@RequestBody @Validated LoginUserDTO apiReq) {
        ApiResp<String> resp = new ApiResp<>();
        int userId = sysUserService.getIdByUsername(apiReq.getUsername());
        operateRecordService.insertNewRecordAsync(userId, String.valueOf(userId), VisitActionEnum.USER_LOGIN, new Date(),
                "User " + apiReq.getUsername() + " try to login in.");
        String token = authService.login(apiReq.getUsername(), apiReq.getPassword(), apiReq.getVisitorIP(), apiReq.getBrowserFingerprint());
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
        SysUserDO sysUser = new SysUserDO();
        apiReq.copyTo(sysUser);
        sysUser.setUsername(apiReq.getUsername());
        sysUser.setEmail(apiReq.getEmail());
        sysUser.setPassword(apiReq.getPassword());
        String salt = UUID.randomUUID().toString().replace("-", "");
        sysUser.initialize(new Date(), String.valueOf(RoleDO.VISITOR), salt);
        ApiResp<Boolean> resp = new ApiResp<>();
        try {
            operateRecordService.insertNewRecordAsync(apiReq.getVisitorId(), apiReq.getUsername(), VisitActionEnum.USER_REGISTER, new Date(),
                    "Visitor try to register " + apiReq.getUsername() + ".");
            boolean success = authService.insertSysUser(sysUser);
            resp.success().setData(success);
        } catch (UserNameOccupiedException ex) {
            resp.setCode(HttpStatus.BAD_REQUEST.value());
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
    @PreAuthorize("hasRole('user')")
    public ApiResp<Boolean> changePassword(@RequestBody @Validated ChangePwdDTO apiReq) {
        SysUserDO curUser = SecurityUtil.getCurrentUser();
        if (curUser == null || !curUser.getUsername().equals(apiReq.getUsername())) {
            throw new UserUnmatchedException();
        }
        int userId = sysUserService.getIdByUsername(apiReq.getUsername());
        operateRecordService.insertNewRecordAsync(userId, String.valueOf(userId), VisitActionEnum.USER_CHANGE_PASSWORD, new Date(),
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
    @PreAuthorize("hasRole('user')")
    public ApiResp<Boolean> changeUserInfo(@RequestBody ChangeDetailDTO apiReq) {
        SysUserDO curUser = SecurityUtil.getCurrentUser();
        if (curUser == null || !curUser.getId().equals(apiReq.getId())) {
            throw new UserUnmatchedException();
        }
        SysUserDO sysUser = sysUserService.selectById(apiReq.getId());
        ApiResp<Boolean> resp = new ApiResp<>();
        if (sysUser == null) {
            resp.setCode(301);
            resp.setMessage("Please check your account carefully.");
            resp.setData(false);
            return resp;
        }
        apiReq.copyTo(sysUser);
        operateRecordService.insertNewRecordAsync(sysUser.getId(), sysUser.getId().toString(), VisitActionEnum.USER_CHANGE_INFORMATION, new Date(),
                "User " + sysUser.getUsername() + " try to change information.");
        boolean result = sysUserService.update(sysUser) > 0;
        if (result) {
            resp.success().setData(true);
        } else {
            resp.setCode(501);
            resp.setMessage("Failure to change information.");
            resp.setData(false);
        }
        return resp;
    }

    @RequestMapping(path = "logout")
    @ApiOperation("用户登出")
    @PreAuthorize("hasRole('user')")
    public ApiResp<Boolean> logOut(@RequestParam String username) {
        ApiResp<Boolean> resp = new ApiResp<Boolean>().success();
        int userId = sysUserService.getIdByUsername(username);
        operateRecordService.insertNewRecordAsync(userId, String.valueOf(userId), VisitActionEnum.USER_LOGOUT, new Date(),
                "User " + username + " try to log out.");
        resp.setData(authService.logOut(username));
        return resp;
    }

    @RequestMapping(path = "apply/resetPwd")
    @ApiOperation("申请重置密码")
    public ApiResp<Boolean> applyResetPassword(@RequestParam @NotNull @Validated String username) {
        ApiResp<Boolean> resp = new ApiResp<>();
        SysUserDO user = (SysUserDO) sysUserService.loadUserByUsername(username);
        if (user == null) {
            resp.setCode(301);
            resp.setMessage("Please check you account carefully.");
            return resp;
        }
        operateRecordService.insertNewRecordAsync(user.getId(), user.getId().toString(), VisitActionEnum.USER_APPLY_RETRIEVE_PASSWORD, new Date(),
                "User " + username + " apply to change password.");
        // 发送邮件到用户注册邮箱
        resp.success().setData(authService.sendResetPasswordEmail(user));
        return resp;
    }

    @RequestMapping(path = "resetPwd")
    @ApiOperation("重置密码")
    @PreAuthorize("hasRole('pwd_resetter')")
    public ApiResp<Boolean> resetPassword(@RequestParam("applyId") String applySerial, @RequestBody ChangePwdDTO changePwd) {
        int userId = sysUserService.getIdByUsername(changePwd.getUsername());
        operateRecordService.insertNewRecordAsync(userId, String.valueOf(userId), VisitActionEnum.USER_RETRIEVE_PASSWORD, new Date(),
                "User " + changePwd.getUsername() + " try to retrieve password.");
        boolean success = authService.resetPassword(changePwd.getUsername(), applySerial, changePwd.getNewPassword());
        ApiResp<Boolean> resp = new ApiResp<>();
        resp.success().setData(success);
        return resp;
    }
    @RequestMapping(path = "retrieve")
    public ApiResp<Boolean> applyRetrieveAccount(@RequestParam("applyEmail") String applyEmail) {
        ApiResp<Boolean> resp = new ApiResp<>();
        resp.setCode(406);
        resp.setMessage("Failure to apply retrieve account.");
        SysUserDO user = (SysUserDO) sysUserService.loadUserByUsername(applyEmail);
        if (user == null) {
            return resp;
        }
        operateRecordService.insertNewRecordAsync(user.getId(), user.getId().toString(), VisitActionEnum.USER_APPLY_RETRIEVE_ACCOUNT, new Date(),
                "User " + user.getUsername() + " apply to change password.");
        // 发送邮件到用户注册邮箱
        resp.success().setData(authService.sendRetrieveAccount(user));
        return resp;
    }
}

package com.peashoot.blog.controller;

import com.peashoot.blog.context.request.sysuser.*;
import com.peashoot.blog.crypto.annotation.DecryptRequest;
import com.peashoot.blog.batis.entity.RoleDO;
import com.peashoot.blog.batis.entity.SysUserDO;
import com.peashoot.blog.context.response.ApiResp;
import com.peashoot.blog.batis.service.AuthService;
import com.peashoot.blog.batis.service.SysUserService;
import com.peashoot.blog.exception.UserNameOccupiedException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@Api(tags = "用户管理相关接口")
@RequestMapping(path = "auth")
public class SysUserController {
    /**
     * 用户信息操作类
     */
    @Autowired
    private SysUserService sysUserService;
    /**
     * 用户认证操作类
     */
    @Autowired
    private AuthService authService;

    /**
     * 根据用户名密码进行登录
     *
     * @param loginUser 用户账户信息
     * @return 带有token的报文
     */
    @PostMapping(path = "login")
    @ApiOperation("根据用户名或者邮箱进行登录")
    @DecryptRequest
    public ApiResp<String> loginByUserNameAndPassword(@RequestBody LoginUserDTO loginUser) {
        ApiResp<String> resp = new ApiResp<>();
        String token = authService.login(loginUser.getUsername(), loginUser.getPassword(), loginUser.getVisitorIP(), loginUser.getBrowserFingerprint());
        resp.success().setData(token);
        resp.setTimestamp(System.currentTimeMillis());
        return resp;
    }

    /**
     * 注册系统用户
     *
     * @param detail 注册信息
     * @return 返回是否注册成功
     */
    @PostMapping(path = "register")
    @ApiOperation("注册用户")
    @DecryptRequest
    public ApiResp<Boolean> registerSysUser(@RequestBody RegisterUserDTO detail) {
        SysUserDO sysUser = new SysUserDO();
        detail.copyTo(sysUser);
        sysUser.setUsername(detail.getUsername());
        sysUser.setEmail(detail.getEmail());
        sysUser.setPassword(detail.getPassword());
        String salt = UUID.randomUUID().toString().replace("-", "");
        sysUser.initialize(new Date(), String.valueOf(RoleDO.VISITOR), salt);
        ApiResp<Boolean> resp = new ApiResp<>();
        try {
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
     * @param changePwd 修改密码
     * @return 修改结果
     */
    @PostMapping(path = "changePwd")
    @ApiOperation("修改用户密码")
    public ApiResp<Boolean> changePassword(@RequestBody ChangePwdDTO changePwd) {
        boolean success = authService.changePassword(changePwd.getUsername(), changePwd.getOldPassword(), changePwd.getNewPassword());
        ApiResp<Boolean> resp = new ApiResp<>();
        resp.success().setData(success);
        return resp;
    }

    /**
     * 修改用户信息
     *
     * @param changeDetail 修改后的用户信息
     * @return 是否修改成功
     */
    @PostMapping(path = "changeDetail")
    @ApiOperation("修改用户信息")
    public ApiResp<Boolean> changeUserInfo(@RequestBody ChangeDetailDTO changeDetail) {
        SysUserDO sysUser = sysUserService.selectById(changeDetail.getId());
        ApiResp<Boolean> resp = new ApiResp<>();
        if (sysUser == null) {
            resp.setCode(301);
            resp.setMessage("Please check your account carefully.");
            resp.setData(false);
            return resp;
        }
        changeDetail.copyTo(sysUser);
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
    public ApiResp<Boolean> logOut(@RequestParam String username) {
        ApiResp<Boolean> resp = new ApiResp<Boolean>().success();
        resp.setData(authService.logOut(username));
        return resp;
    }

    @RequestMapping(path = "apply/resetPwd")
    @ApiOperation("申请重置密码")
    public ApiResp<Boolean> applyResetPassword(@RequestParam String username) {
        ApiResp<Boolean> resp = new ApiResp<>();
        SysUserDO user = (SysUserDO) sysUserService.loadUserByUsername(username);
        if (user == null) {
            resp.setCode(301);
            resp.setMessage("Please check you account carefully.");
            return resp;
        }
        // 发送邮件到用户注册邮箱
        resp.success().setData(authService.sendResetPasswordEmail(user));
        return resp;
    }

    @RequestMapping(path = "resetPwd")
    @ApiOperation("重置密码")
    public ApiResp<Boolean> resetPassword(@RequestParam("applyId") String applySerial, @RequestBody ChangePwdDTO changePwd) {
        boolean success = authService.resetPassword(changePwd.getUsername(), applySerial, changePwd.getNewPassword());
        ApiResp<Boolean> resp = new ApiResp<>();
        resp.success().setData(success);
        return resp;
    }
}

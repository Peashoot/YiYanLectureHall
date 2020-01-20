package com.peashoot.blog.controller;

import com.peashoot.blog.context.request.sysuser.ChangePwd;
import com.peashoot.blog.crypto.annotation.DecryptRequest;
import com.peashoot.blog.crypto.annotation.EncryptResponse;
import com.peashoot.blog.batis.entity.Role;
import com.peashoot.blog.batis.entity.SysUser;
import com.peashoot.blog.context.response.ApiResp;
import com.peashoot.blog.context.request.sysuser.LoginUser;
import com.peashoot.blog.context.request.sysuser.RegisterUser;
import com.peashoot.blog.batis.service.AuthService;
import com.peashoot.blog.batis.service.SysUserService;
import com.peashoot.blog.exception.UserHasLoginException;
import com.peashoot.blog.exception.UserNameOccupiedException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * 系统用户控制器
 *
 * @Author peashoot
 */
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
    public ApiResp<String> loginByUserNameAndPassword(@RequestBody LoginUser loginUser) {
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
    public ApiResp<Boolean> registerSysUser(@RequestBody RegisterUser detail) {
        SysUser sysUser = new SysUser();
        detail.copyTo(sysUser);
        sysUser.setPassword(detail.getPassword());
        String salt = UUID.randomUUID().toString().replace("-", "");
        sysUser.initialize(new Date(), String.valueOf(Role.VISITOR), salt);
        ApiResp<Boolean> resp = new ApiResp<>();
        try {
            boolean success = authService.register(sysUser);
            resp.success().setData(success);
        } catch (UserNameOccupiedException ex) {
            resp.setCode(HttpStatus.BAD_REQUEST.value());
            resp.setMessage("failure");
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
    public ApiResp<Boolean> changePassword(@RequestBody ChangePwd changePwd) {
        boolean success = authService.changePassword(changePwd.getUsername(), changePwd.getOldPassword(), changePwd.getNewPassword());
        ApiResp<Boolean> resp = new ApiResp<>();
        resp.success().setData(success);
        return resp;
    }
}

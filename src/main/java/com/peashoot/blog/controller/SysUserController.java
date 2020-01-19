package com.peashoot.blog.controller;

import com.peashoot.blog.crypto.annotation.DecryptRequest;
import com.peashoot.blog.crypto.annotation.EncryptResponse;
import com.peashoot.blog.batis.entity.Role;
import com.peashoot.blog.batis.entity.SysUser;
import com.peashoot.blog.context.response.ApiResp;
import com.peashoot.blog.context.request.sysuser.LoginUser;
import com.peashoot.blog.context.request.sysuser.RegisterUser;
import com.peashoot.blog.batis.service.AuthService;
import com.peashoot.blog.batis.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
@Api(tags = "用户管理相关接口")
@RequestMapping()
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private AuthService authService;

    @PostMapping(value = "/auth/login")
    @ApiOperation("根据用户名或者邮箱进行登录")
    @DecryptRequest
    @EncryptResponse
    public ApiResp<String> loginByUserNameAndPassword(@RequestBody LoginUser loginUser) {
        String token = authService.login(loginUser.getUsername(), loginUser.getPassword());
        ApiResp<String> resp = new ApiResp<>();
        resp.success();
        resp.setToken(token);
        return resp;
    }

    @PostMapping("/auth/register")
    @ApiOperation("注册用户")
    public ApiResp<Boolean> registerSysUser(@RequestBody RegisterUser detail) {
        SysUser sysUser = new SysUser();
        detail.copyTo(sysUser);
        sysUser.setPassword(detail.getPassword());
        String salt = UUID.randomUUID().toString().replace("-", "");
        sysUser.initialize(new Date(), String.valueOf(Role.VISITOR), salt);
        boolean success = authService.register(sysUser);
        ApiResp<Boolean> resp = new ApiResp<>();
        resp.success();
        resp.setData(success);
        return resp;
    }
}

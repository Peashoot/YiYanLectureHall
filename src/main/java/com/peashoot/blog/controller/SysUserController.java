package com.peashoot.blog.controller;

import com.peashoot.blog.entity.Role;
import com.peashoot.blog.entity.SysUser;
import com.peashoot.blog.response.ApiResp;
import com.peashoot.blog.request.sysuser.LoginUser;
import com.peashoot.blog.request.sysuser.RegisterUser;
import com.peashoot.blog.service.AuthService;
import com.peashoot.blog.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping()
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private AuthService authService;

    @PostMapping(value = "/auth/login")
    public ApiResp<String> loginByUserNameAndPassword(@RequestBody LoginUser loginUser) {
        String token = authService.login(loginUser.getUsername(), loginUser.getPassword());
        ApiResp<String> resp = new ApiResp<>();
        resp.success();
        resp.setToken(token);
        return resp;
    }

    @PostMapping("/auth/register")
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

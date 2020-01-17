package com.peashoot.blog.request.sysuser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginUser {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}

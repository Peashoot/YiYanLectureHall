package com.peashoot.blog.request.sysuser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUser extends SysUserDetail {
    /**
     * 密码
     */
    private String password;
}

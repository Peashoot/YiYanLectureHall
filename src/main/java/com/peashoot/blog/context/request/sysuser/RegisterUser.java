package com.peashoot.blog.context.request.sysuser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel
public class RegisterUser extends SysUserDetail {
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", required = true)
    private String password;
}

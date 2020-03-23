package com.peashoot.blog.context.request.sysuser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel
public class RegisterUserDTO extends SysUserDetailDTO {
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", required = true)
    private String email;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", required = true)
    private String password;
}

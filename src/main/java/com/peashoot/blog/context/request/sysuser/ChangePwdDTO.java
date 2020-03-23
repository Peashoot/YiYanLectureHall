package com.peashoot.blog.context.request.sysuser;

import com.peashoot.blog.context.request.BaseApiReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel
public class ChangePwdDTO extends BaseApiReq {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名或邮箱", required = true)
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(value = "旧密码", required = true)
    private String oldPassword;
    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码", required = true)
    private String newPassword;
}

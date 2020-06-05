package com.peashoot.blog.context.request.sysuser;

import com.peashoot.blog.context.request.BaseApiReq;
import com.peashoot.blog.util.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@ApiModel
@ToString(callSuper = true, includeFieldNames = false)
public class ChangePwdDTO extends BaseApiReq {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名或邮箱", required = true)
    @Pattern(message = "illegal username", regexp = Constant.PATTERN_CHECK_USERNAME_OR_EMAIL)
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(value = "旧密码", required = true)
    @Pattern(message = "no original password", regexp = Constant.PATTERN_CHECK_PASSWORD)
    private String oldPassword;
    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码", required = true)
    @Pattern(message = "no original password", regexp = Constant.PATTERN_CHECK_PASSWORD)
    private String newPassword;
}

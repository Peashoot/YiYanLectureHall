package com.peashoot.blog.context.request.sysuser;

import com.peashoot.blog.context.request.BaseApiReq;
import com.peashoot.blog.util.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@ApiModel
@ToString(callSuper = true, includeFieldNames = false)
public class LoginUserDTO extends BaseApiReq {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名或邮箱", required = true)
    @Pattern(message = "illegal username", regexp = Constant.PATTERN_CHECK_USERNAME_OR_EMAIL)
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", required = true)
    @Pattern(message = "no original password", regexp = Constant.PATTERN_CHECK_PASSWORD)
    private String password;
}

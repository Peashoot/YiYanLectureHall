package com.peashoot.blog.context.request.sysuser;

import com.peashoot.blog.util.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;

@Getter
@Setter
@ApiModel
@ToString(callSuper = true, includeFieldNames = false)
public class RegisterUserDTO extends SysUserDetailDTO {
    /**
     * 访客id
     */
    @ApiModelProperty(value = "访客Id")
    private Long visitorId;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", required = true)
    @Email(message = "incorrect email address", regexp = Constant.PATTERN_CHECK_EMAIL)
    private String email;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    @Pattern(message = "illegal username", regexp = Constant.PATTERN_CHECK_USERNAME)
    private String username;
    /**
     * 密码(md5加密后的内容，非明文)
     */
    @ApiModelProperty(value = "密码", required = true)
    @Pattern(message = "no original password", regexp = Constant.PATTERN_CHECK_PASSWORD)
    private String password;
}

package com.peashoot.blog.context.request.sysuser;

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
    private Long visitorId;
    /**
     * 邮箱
     */
    @Email(message = "", regexp = "^[A-Za-z0-9!#$%&'+/=?^_`{|}~-]+(.[A-Za-z0-9!#$%&'+/=?^_`{|}~-]+)*" +
            "@([A-Za-z0-9]+(?:-[A-Za-z0-9]+)?.)+[A-Za-z0-9]+(-[A-Za-z0-9]+)?$")
    @ApiModelProperty(value = "邮箱", required = true)
    private String email;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    private String username;
    /**
     * 密码(md5加密后的内容，非明文)
     */
    @Pattern(regexp = "^[0-9a-fA-F]{32}$")
    @ApiModelProperty(value = "密码", required = true)
    private String password;
}

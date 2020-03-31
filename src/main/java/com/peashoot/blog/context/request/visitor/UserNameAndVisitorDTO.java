package com.peashoot.blog.context.request.visitor;

import com.peashoot.blog.context.request.BaseApiReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = false)
public class UserNameAndVisitorDTO extends BaseApiReq {
    /**
     * 访客名称
     */
    @NotNull
    @ApiModelProperty(value = "访客名称", required = true)
    private String visitorName;
    /**
     * 系统用户名称
     */
    @NotNull
    @ApiModelProperty(value = "系统用户名称", required = true)
    private String sysUsername;
}

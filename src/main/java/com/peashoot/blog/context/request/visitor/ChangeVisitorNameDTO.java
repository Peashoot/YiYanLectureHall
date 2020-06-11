package com.peashoot.blog.context.request.visitor;

import com.peashoot.blog.context.request.BaseApiReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = false)
public class ChangeVisitorNameDTO extends BaseApiReq {
    /**
     * 旧的访客名称
     */
    @NotNull
    @ApiModelProperty("旧的访客名称，根据该名称匹配访客记录")
    private String oldVisitorName;
    /**
     * 新的访客名称
     */
    @NotNull
    @ApiModelProperty("新的访客名称")
    private String newVisitorName;
}

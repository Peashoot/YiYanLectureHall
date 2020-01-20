package com.peashoot.blog.context.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel
public class ApiReq {
    /**
     * 访问IP
     */
    @ApiModelProperty(value = "访问IP")
    private String visitorIP;
    /**
     * 浏览器指纹
     */
    @ApiModelProperty(value = "浏览器指纹")
    private String browserFingerprint;
}
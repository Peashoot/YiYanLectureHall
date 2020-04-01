package com.peashoot.blog.context.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@ApiModel
public abstract class BaseApiReq {
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
    /**
     * 访问时间
     */
    @ApiModelProperty(value = "访问时间，时间戳")
    private Date visitTime;
}
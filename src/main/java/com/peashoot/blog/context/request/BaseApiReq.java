package com.peashoot.blog.context.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter
@Setter
@ApiModel
public abstract class BaseApiReq {
    /**
     * 访问IP
     */
    @NotNull("${peashoot.blog.http.content.request.params.missing}")
    @NotEmpty(message = "${peashoot.blog.http.content.request.params.missing}")
    @ApiModelProperty(value = "访问IP")
    private String visitorIp;
    /**
     * 浏览器指纹
     */
    @NotNull("${peashoot.blog.http.content.request.params.missing}")
    @NotEmpty(message = "${peashoot.blog.http.content.request.params.missing}")
    @ApiModelProperty(value = "浏览器指纹")
    private String browserFingerprint;
    /**
     * 访问时间
     */
    @NotNull("${peashoot.blog.http.content.request.params.missing}")
    @ApiModelProperty(value = "访问时间，时间戳")
    private Date visitTime;
}
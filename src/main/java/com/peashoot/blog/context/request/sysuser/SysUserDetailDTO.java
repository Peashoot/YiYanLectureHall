package com.peashoot.blog.context.request.sysuser;

import com.peashoot.blog.batis.entity.SysUserDO;
import com.peashoot.blog.context.request.BaseApiReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel
public class SysUserDetailDTO extends BaseApiReq {
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称", required = true)
    private String nickname;
    /**
     * 所在地区
     */
    @ApiModelProperty(value = "所在地区")
    private String location;
    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String contract;
    /**
     * qq
     */
    @ApiModelProperty(value = "qq")
    private String qq;
    /**
     * 头像照片（网络路径）
     */
    @ApiModelProperty(value = "头像照片（网络路径）")
    private String headPortrait;
    /**
     * 性别： 0 保密 1 男 2 女
     */
    @ApiModelProperty(value = "性别： 0 保密 1 男 2 女", required = true)
    private int gender;
    /**
     * 个人简介
     */
    @ApiModelProperty(value = "个人简介")
    private String personalProfile;
    /**
     * 将信息复制到系统用户中
     * @param sysUser 系统用户实体
     */
    public void copyTo(@NotNull SysUserDO sysUser) {
        sysUser.setNickName(nickname);
        sysUser.setLocation(location);
        sysUser.setContact(contract);
        sysUser.setQq(qq);
        sysUser.setHeadPortrait(headPortrait);
        sysUser.setGender(gender);
        sysUser.setPersonalProfile(personalProfile);
    }
}

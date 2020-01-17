package com.peashoot.blog.request.sysuser;

import com.peashoot.blog.entity.SysUser;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SysUserDetail {
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 所在地区
     */
    private String location;
    /**
     * 联系方式
     */
    private String contract;
    /**
     * qq
     */
    private String qq;
    /**
     * 头像照片（网络路径）
     */
    private String headPortrait;
    /**
     * 性别： 0 保密 1 男 2 女
     */
    private int gender;
    /**
     * 个人简介
     */
    private String personalProfile;
    /**
     * 将信息复制到系统用户中
     * @param sysUser 系统用户实体
     */
    public void copyTo(@NotNull SysUser sysUser) {
        sysUser.setUsername(username);
        sysUser.setNickName(nickname);
        sysUser.setEmail(email);
        sysUser.setLocation(location);
        sysUser.setContact(contract);
        sysUser.setQq(qq);
        sysUser.setHeadPortrait(headPortrait);
        sysUser.setGender(gender);
        sysUser.setPersonalProfile(personalProfile);
    }
}

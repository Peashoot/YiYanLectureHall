package com.peashoot.blog.batis.entity;

import com.peashoot.blog.batis.entity.base.IntPrimaryEntity;
import com.peashoot.blog.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SysUser extends IntPrimaryEntity implements UserDetails {
    /**
     * 角色类型id
     */
    private String roleIds;
    /**
     * 角色类型
     */
    private List<Role> roles;
    /**
     * 用户名
     */
    private String username;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 盐加密密钥
     */
    private String salt;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 联系方式
     */
    private String contact;
    /**
     * qq
     */
    private String qq;
    /**
     * 上一次登录时间
     */
    private Date lastLogin;
    /**
     * 注册时间
     */
    private Date registerTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 帐户未过期
     */
    private boolean accountNonExpired;
    /**
     * 帐户未锁定
     */
    private boolean accountNonLocked;
    /**
     * 凭证未过期
     */
    private boolean credentialsNonExpired;
    /**
     * 已启用
     */
    private boolean enabled;
    /**
     * 密码被重置的日期
     */
    private Date lastPasswordResetDate;
    /**
     * 所在地区
     */
    private String location;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<>();
        List<Role> roles = this.getRoles();
        if (roles != null) {
            for (Role role : roles) {
                if (role == null) {
                    continue;
                }
                auths.add(new SimpleGrantedAuthority(role.getRoleName()));
            }
        }
        return auths;
    }

    /**
     * 密码加盐混合
     * @return 混合后的密钥
     */
    public String saltPatchWork() {
        return StringUtils.mixSaltWithPassword(password, salt);
    }
    /**
     * 初始化参数
     * @param initDate 初始化日期
     * @param roleIds 角色id
     * @param salt 盐
     */
    public void initialize(Date initDate, String roleIds, String salt) {
        registerTime = lastLogin = lastPasswordResetDate = updateTime = initDate;
        enabled = credentialsNonExpired = accountNonLocked = accountNonExpired = true;
        this.roleIds = roleIds;
        this.salt = salt;
    }
}
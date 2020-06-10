package com.peashoot.blog.batis.entity;

import com.peashoot.blog.batis.entity.base.BaseEntity;
import com.peashoot.blog.batis.entity.base.IntPrimaryEntity;
import com.peashoot.blog.batis.enums.GenderEnum;
import com.peashoot.blog.batis.enums.PermissionEnum;
import com.peashoot.blog.util.Constant;
import com.peashoot.blog.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = false)
public class SysUserDO extends IntPrimaryEntity implements UserDetails {
    /**
     * 角色类型
     */
    private List<RoleDO> roles;
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
     * ems或者qq等社交账号
     */
    private String socialAccount;
    /**
     * 上一次登录时间
     */
    private Date lastLoginTime;
    /**
     * 上一次登录IP
     */
    private String lastLoginIp;
    /**
     * 注册时间
     */
    private Date registerTime;
    /**
     * 注册IP
     */
    private String registerIp;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 帐户未过期
     */
    private Date accountExpiredTime;
    /**
     * 帐户未锁定
     */
    private Date accountLockedTime;
    /**
     * 凭证未过期
     */
    private Date credentialsExpiredTime;
    /**
     * 已启用
     */
    private Date enabledTime;
    /**
     * 密码被重置的日期
     */
    private Date lastPasswordResetTime;
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
    private GenderEnum gender;
    /**
     * 个人简介
     */
    private String personalProfile;
    /**
     * 角色id集合
     */
    private String roleIds;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<>();
        List<RoleDO> roles = this.getRoles();
        if (roles != null) {
            for (RoleDO role : roles) {
                if (role == null) {
                    continue;
                }
                if (role.getPermissions() != null) {
                    for (PermissionEnum permission : role.getPermissions()) {
                        auths.add(new SimpleGrantedAuthority(permission.getValue()));
                    }
                }
            }
        }
        return auths;
    }

    /**
     * 密码加盐混合
     *
     * @return 混合后的密钥
     */
    public String saltPatchWork() {
        return StringUtils.mixSaltWithPassword(password, salt);
    }

    /**
     * 用户注册后默认超期时间
     */
    private final Long expireTime = 30L * Constant.DAYS_PEY_YEAR * Constant.HOURS_PEY_DAY * Constant.MINUTES_PEY_HOUR * Constant.SECONDS_PEY_MINUTES * Constant.MILLISECONDS_PEY_SECOND;

    /**
     * 初始化参数
     *
     * @param initDate 初始化日期
     * @param roleArray  角色数组
     * @param salt     盐
     */
    public void initialize(Date initDate, List<RoleDO> roleArray, String salt) {
        registerTime = lastLoginTime = lastPasswordResetTime = updateTime = new Date();
        credentialsExpiredTime = accountExpiredTime = accountLockedTime = new Date(System.currentTimeMillis() + expireTime);
        enabledTime = new Date();
        this.roles = roleArray;
        this.salt = salt;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsExpiredTime.getTime() >= System.currentTimeMillis();
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountExpiredTime.getTime() >= System.currentTimeMillis();
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountLockedTime.getTime() >= System.currentTimeMillis();
    }

    @Override
    public boolean isEnabled() {
        return enabledTime.getTime() <= System.currentTimeMillis();
    }

    /**
     * 拼接角色id列表
     */
    public void concatRoleIds() {
        roleIds = roles.stream().map(i -> i.getId().toString()).collect(Collectors.joining(","));
    }
}
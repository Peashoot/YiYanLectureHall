package com.peashoot.blog.entity;

import com.peashoot.blog.entity.base.LongPrimaryEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Visitor extends LongPrimaryEntity {
    /**
     * jwt生成的token
     */
    private String token;
    /**
     * 访客登录时间
     */
    private Date loginTime;
    /**
     * 系统用户登录后过期时间为2小时；访客登录过期时间1个月
     */
    private Date expireTime;
    /**
     * 访客访问的ip地址（ipv4或ipv6）
     */
    private String ip;
    /**
     * 根据ip地址查询到的访问地址
     */
    private String location;
    /**
     * 访客使用的浏览器
     */
    private String browser;
    /**
     * 访客使用的操作系统
     */
    private String os;
    /**
     * 访客的角色类型id
     */
    private Integer roleId;
    /**
     * 访客的角色类型
     */
    private Role role;
    /**
     * 系统用户id
     */
    private Integer userId;
    /**
     * 系统用户
     */
    private SysUser user;
}
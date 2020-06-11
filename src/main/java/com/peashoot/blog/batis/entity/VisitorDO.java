package com.peashoot.blog.batis.entity;

import com.peashoot.blog.batis.entity.base.LongPrimaryEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = false)
public class VisitorDO extends LongPrimaryEntity {
    /**
     * 访客名称
     */
    private String visitor;
    /**
     * 系统用户账号信息
     */
    private String sysUserName;
    /**
     * 系统用户昵称
     */
    private String sysUserNickName;
    /**
     * 访客登录时间
     */
    private Date firstVisitTime;
    /**
     * 访客访问的ip地址（ipv4或ipv6）
     */
    private String visitFromIp;
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
}
package com.peashoot.blog.batis.entity;

import com.peashoot.blog.batis.entity.base.IntPrimaryEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RoleDO extends IntPrimaryEntity {
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 新增时间
     */
    private Date insertTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 权限
     */
    private String permission;
    /**
     * 新增的用户id
     */
    private Integer insertUserId;
    /**
     * 修改的用户id
     */
    private Integer updateUserId;
    /**
     * 新增的用户
     */
    private SysUserDO insertUser;
    /**
     * 修改的用户
     */
    private SysUserDO updateUser;
    /**
     * 访问者
     */
    public static final int VISITOR = 9999;
}
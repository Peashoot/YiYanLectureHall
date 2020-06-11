package com.peashoot.blog.batis.entity;

import com.peashoot.blog.batis.entity.base.IntPrimaryEntity;
import com.peashoot.blog.batis.enums.PermissionEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = false)
public class RoleDO extends IntPrimaryEntity {
    public RoleDO() {
        id = 0;
    }
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
     * 新增的用户id
     */
    private Integer insertUserId;
    /**
     * 新增的用户id
     */
    private SysUserDO insertUser;
    /**
     * 修改的用户id
     */
    private Integer updateUserId;
    /**
     * 修改的用户
     */
    private SysUserDO updateUser;
    /**
     * 权限
     */
    private PermissionEnum[] permissions;
    /**
     * 普通用户
     */
    public static final String ROLE_NORMAL_USER = "ROLE_NORMAL_USER";
    /**
     * 文章管理员
     */
    public static final String ROLE_OF_ARTICLE_MANAGER = "ROLE_ARTICLES_MANAGER";
}
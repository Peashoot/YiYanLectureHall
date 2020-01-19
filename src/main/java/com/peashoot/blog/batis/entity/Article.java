package com.peashoot.blog.batis.entity;

import com.peashoot.blog.batis.entity.base.IntPrimaryEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Article extends IntPrimaryEntity {
    /**
     * 标题
     */
    private String title;
    /**
     * 关键字1
     */
    private String keyword1;
    /**
     * 关键字2
     */
    private String keyword2;
    /**
     * 关键字3
     */
    private String keyword3;
    /**
     * 关键字4
     */
    private String keyword4;
    /**
     * 关键字5
     */
    private String keyword5;
    /**
     * 文件相对路径
     */
    private String filePath;
    /**
     * 文件创建日期
     */
    private Date createTime;
    /**
     * 文件创建人id
     */
    private Integer createUserId;
    /**
     * 文件创建人
     */
    private SysUser createUser;
    /**
     * 文件修改日期
     */
    private Date modifyTime;
    /**
     * 文件修改人id
     */
    private Integer modifyUserId;
    /**
     * 文件修改人
     */
    private SysUser modifyUser;
    /**
     * 文件状态：0: 编辑中；200: 已发布; 404: 已删除
     */
    private Integer status;
    /**
     * 文件概述
     */
    private String overview;
    /**
     * 浏览量
     */
    private Long pageView;
}
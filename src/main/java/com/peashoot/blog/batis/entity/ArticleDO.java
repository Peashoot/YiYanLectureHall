package com.peashoot.blog.batis.entity;

import com.peashoot.blog.batis.entity.base.UUIDPrimaryEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = false)
public class ArticleDO extends UUIDPrimaryEntity {
    public ArticleDO() {
        pageView = 0L;
        supportCount = 0;
        againstCount = 0;
    }

    /**
     * 作者
     */
    private String author;
    /**
     * 标题
     */
    private String title;
    /**
     * 关键字（多个关键字用';'隔开）
     */
    private String keywords;
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
    private SysUserDO createUser;
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
    private SysUserDO modifyUser;
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
    /**
     * 文章URL链接
     */
    private String articleUrl;
    /**
     * 赞成数
     */
    private Integer supportCount;
    /**
     * 反对数
     */
    private Integer againstCount;
}
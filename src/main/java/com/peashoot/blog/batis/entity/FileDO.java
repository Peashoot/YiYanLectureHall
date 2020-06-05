package com.peashoot.blog.batis.entity;

import com.peashoot.blog.batis.entity.base.UUIDPrimaryEntity;
import com.peashoot.blog.batis.enums.FileTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FileDO extends UUIDPrimaryEntity {
    /**
     * 创建者Id（访客id）
     */
    private Long creatorId;
    /**
     * 系统管理员id
     */
    private int sysUserId;
    /**
     * 原始文件名
     */
    private String originalName;
    /**
     * 文件类型
     */
    private FileTypeEnum type;
    /**
     * 本地路径
     */
    private String localPath;
    /**
     * 网络路径
     */
    private String netPath;
    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 原始网络路径
     */
    private String originalNetUrl;
    /**
     * MD5签名
     */
    private String md5Sign;

    private FileDO() {
        createTime = new Date();
    }

    public FileDO(long visitorId, int sysUserId, FileTypeEnum type, String localPath, String netPath, String md5Sign) {
        this();
        this.creatorId = visitorId;
        this.sysUserId = sysUserId;
        this.localPath = localPath;
        this.md5Sign = md5Sign;
        this.type = type;
        this.netPath = netPath;
    }
}

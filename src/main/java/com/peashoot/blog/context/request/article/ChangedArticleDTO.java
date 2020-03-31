package com.peashoot.blog.context.request.article;

import com.peashoot.blog.batis.entity.ArticleDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = false)
public class ChangedArticleDTO {
    /**
     * 记录id
     */
    private String id;
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
     * 作者
     */
    private String author;
    /**
     * 文件相对路径
     */
    private String filePath;
    /**
     * 文件状态：0: 编辑中；200: 已发布; 404: 已删除
     */
    private Integer status;
    /**
     * 文件概述
     */
    private String overview;
    /**
     * 文章URL链接
     */
    private String articleURL;

    /**
     * 将修改信息保存到数据库实体中
     * @param articleEntity 数据库实体
     */
    public void copyTo(ArticleDO articleEntity) {
        articleEntity.setArticleURL(articleURL);
        articleEntity.setAuthor(author);
        articleEntity.setFilePath(filePath);
        articleEntity.setKeyword1(keyword1);
        articleEntity.setKeyword2(keyword2);
        articleEntity.setKeyword3(keyword3);
        articleEntity.setKeyword4(keyword4);
        articleEntity.setKeyword5(keyword5);
        articleEntity.setOverview(overview);
        articleEntity.setStatus(status);
        articleEntity.setTitle(title);
        articleEntity.setModifyTime(new Date());
    }
}

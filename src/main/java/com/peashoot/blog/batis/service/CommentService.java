package com.peashoot.blog.batis.service;

import com.peashoot.blog.batis.entity.CommentDO;
import com.peashoot.blog.batis.service.base.InsertService;
import com.peashoot.blog.batis.service.base.RemoveService;
import com.peashoot.blog.batis.service.base.SelectService;

import java.util.List;

public interface CommentService extends InsertService<CommentDO>,
        SelectService<CommentDO, Long>, RemoveService<CommentDO, Long> {
    /**
     * 获取分页记录
     *
     * @param pageSize  单页最大容量
     * @param pageIndex 当前页码
     * @param articleId 文章id
     * @return 评论列表
     */
    List<CommentDO> listPagedComments(int pageSize, int pageIndex, String articleId);

    /**
     * 获取所有评论数
     *
     * @param articleId 文章id
     * @return 评论总数
     */
    int countTotalRecords(String articleId);

    /**
     * 更新评论点赞数和反对数
     * @param commentId 评论id
     * @param agree 点赞数增量
     * @param disagree 反对数增量
     * @return 是否更新成功
     */
    boolean updateSupportAndDisagreeState(long commentId, int agree, int disagree);
}

package com.peashoot.blog.batis.service.impl;

import com.peashoot.blog.batis.entity.ArticleDO;
import com.peashoot.blog.batis.entity.CommentDO;
import com.peashoot.blog.batis.mapper.ArticleMapper;
import com.peashoot.blog.batis.mapper.SysUserMapper;
import com.peashoot.blog.batis.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {
    private final ArticleMapper articleMapper;

    public ArticleServiceImpl(ArticleMapper articleMapper, SysUserMapper sysuserMapper) {
        this.articleMapper = articleMapper;
    }

    @Override
    public int insert(ArticleDO insertItem) {
        if (insertItem.getCreateUser() != null) {
            insertItem.setCreateUserId(insertItem.getCreateUser().getId());
            insertItem.setModifyUserId(insertItem.getCreateUser().getId());
        }
        insertItem.setModifyTime(insertItem.getCreateTime());
        return articleMapper.insert(insertItem);
    }

    @Override
    public int remove(String removeId) {
        return articleMapper.deleteByPrimaryKey(removeId);
    }

    @Override
    public int removeRange(List<String> removeIdList) {
        return articleMapper.deleteRangeByPrimaryKeys(removeIdList.toArray(new Integer[0]));
    }

    @Override
    public List<ArticleDO> selectAll() {
        return articleMapper.selectAll();
    }

    @Override
    public ArticleDO selectById(String id) {
        return articleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(ArticleDO updateItem) {
        if (updateItem.getModifyUser() != null) {
            updateItem.setModifyUserId(updateItem.getModifyUser().getId());
        }
        return articleMapper.updateByPrimaryKey(updateItem);
    }

    @Override
    public List<ArticleDO> listPagedArticles(int pageSize, int pageIndex, String authorLike, String keywordLike, String titleLike) {
        return articleMapper.listPagedArticles(pageSize * pageIndex, pageSize, authorLike, keywordLike, titleLike);
    }

    @Override
    public int countTotalRecords(String authorLike, String keywordLike, String titleLike) {
        return articleMapper.countTotalRecords(authorLike, keywordLike, titleLike);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public boolean updateSupportAndDisagreeState(String articleId, int agree, int disagree) {
        ArticleDO articleDO = articleMapper.selectByPrimaryKeyForUpdate(articleId);
        articleDO.setSupportCount(articleDO.getSupportCount() + agree);
        articleDO.setAgainstCount(articleDO.getAgainstCount() + disagree);
        return articleMapper.updateByPrimaryKey(articleDO) > 0;
    }
}

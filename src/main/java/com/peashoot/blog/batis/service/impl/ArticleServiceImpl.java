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
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private SysUserMapper sysuserMapper;

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
        List<ArticleDO> retList = articleMapper.selectAll();
        for (ArticleDO article : retList) {
            article.setCreateUser(sysuserMapper.selectByPrimaryKey(article.getCreateUserId()));
            article.setModifyUser(sysuserMapper.selectByPrimaryKey(article.getModifyUserId()));
        }
        return retList;
    }

    @Override
    public ArticleDO selectById(String id) {
        ArticleDO article = articleMapper.selectByPrimaryKey(id);
        article.setCreateUser(sysuserMapper.selectByPrimaryKey(article.getCreateUserId()));
        article.setModifyUser(sysuserMapper.selectByPrimaryKey(article.getModifyUserId()));
        return article;
    }

    @Override
    public int update(ArticleDO updateItem) {
        if (updateItem.getCreateUser() != null) {
            updateItem.setCreateUserId(updateItem.getCreateUser().getId());
        }
        if (updateItem.getModifyUser() != null) {
            updateItem.setModifyUserId(updateItem.getModifyUser().getId());
        }
        return articleMapper.updateByPrimaryKey(updateItem);
    }

    @Override
    public List<ArticleDO> listPagedArticles(int pageSize, int pageIndex, String authorLike, String keywordLike, String titleLike) {
        return articleMapper.listPagedArticles(pageSize * pageIndex, pageIndex, authorLike, keywordLike, titleLike);
    }

    @Override
    public int countTotalRecords(String keywordLike, String authorLike, String titleLike) {
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

package com.peashoot.blog.batis.service.impl;

import com.peashoot.blog.batis.entity.Article;
import com.peashoot.blog.batis.mapper.ArticleMapper;
import com.peashoot.blog.batis.mapper.SysUserMapper;
import com.peashoot.blog.batis.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private SysUserMapper sysuserMapper;

    @Override
    public int insert(Article insertItem) {
       if (insertItem.getCreateUser() != null) {
           insertItem.setCreateUserId(insertItem.getCreateUser().getId());
           insertItem.setModifyUserId(insertItem.getCreateUser().getId());
       }
       insertItem.setModifyTime(insertItem.getCreateTime());
       return articleMapper.insert(insertItem);
    }

    @Override
    public int remove(Integer removeId) {
        return articleMapper.deleteByPrimaryKey(removeId);
    }

    @Override
    public int removeRange(List<Integer> removeIdList) {
        return articleMapper.deleteRangeByPrimaryKeys(removeIdList.toArray(new Integer[0]));
    }

    @Override
    public List<Article> selectAll() {
        List<Article> retList = articleMapper.selectAll();
        for (Article article : retList) {
            article.setCreateUser(sysuserMapper.selectByPrimaryKey(article.getCreateUserId()));
            article.setModifyUser(sysuserMapper.selectByPrimaryKey(article.getModifyUserId()));
        }
        return retList;
    }

    @Override
    public Article selectById(Integer id) {
        Article article = articleMapper.selectByPrimaryKey(id);
        article.setCreateUser(sysuserMapper.selectByPrimaryKey(article.getCreateUserId()));
        article.setModifyUser(sysuserMapper.selectByPrimaryKey(article.getModifyUserId()));
        return article;
    }

    @Override
    public int update(Article updateItem) {
        if (updateItem.getCreateUser() != null) {
            updateItem.setCreateUserId(updateItem.getCreateUser().getId());
        }
        if (updateItem.getModifyUser() != null) {
            updateItem.setModifyUserId(updateItem.getModifyUser().getId());
        }
        return articleMapper.updateByPrimaryKey(updateItem);
    }

    @Override
    public List<Article> getArticlesByPage(int pageSize, int pageIndex, String keywordLike, String titleLike) {
        return articleMapper.getArticlesByPage(pageSize * pageIndex, pageIndex, keywordLike, titleLike);
    }

    @Override
    public int getMatchedArticleTotalCount(String keywordLike, String titleLike) {
        return articleMapper.getMatchedArticleTotalCount(keywordLike, titleLike);
    }
}

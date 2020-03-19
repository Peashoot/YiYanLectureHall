package com.peashoot.blog.controller;

import com.peashoot.blog.batis.entity.Article;
import com.peashoot.blog.batis.entity.SysUser;
import com.peashoot.blog.batis.service.ArticleService;
import com.peashoot.blog.batis.service.SysUserService;
import com.peashoot.blog.context.request.article.ArticleSearch;
import com.peashoot.blog.context.request.article.ChangedArticle;
import com.peashoot.blog.context.response.ApiResp;
import com.peashoot.blog.context.response.article.ArticleIntroduction;
import com.peashoot.blog.context.response.article.ArticlesCollection;
import com.peashoot.blog.util.SecurityUtil;
import io.swagger.annotations.Api;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "文章信息操作接口")
public class ArticleController {
    /**
     * Article操作类
     */
    @Autowired
    private ArticleService articleService;
    /**
     * 系统用户信息操作类
     */
    @Autowired
    private SysUserService sysUserService;

    /**
     * 根据查询条件获取符合条件的Article
     *
     * @param searchCondition 查询条件
     * @return 符合条件的Article
     */
    @RequestMapping
    public ApiResp<ArticlesCollection> getArticles(@RequestBody ArticleSearch searchCondition) {
        List<Article> matchedList = articleService.getArticlesByPage(searchCondition.getPageSize(), searchCondition.getPageIndex(), searchCondition.getAuthorLike(), searchCondition.getKeywordLike(), searchCondition.getTitleLike());
        int totalCount = articleService.getMatchedArticleTotalCount(searchCondition.getAuthorLike(), searchCondition.getKeywordLike(), searchCondition.getTitleLike());
        ApiResp<ArticlesCollection> retResp = new ApiResp<ArticlesCollection>().success();
        ArticlesCollection data = new ArticlesCollection();
        data.setArticleList(matchedList.stream().map(a -> ArticleIntroduction.createArticlesInfo(a)).collect(Collectors.toList()));
        data.setPageSize(searchCondition.getPageSize());
        data.setPageIndex(searchCondition.getPageIndex());
        data.setTotalRecordsCount(totalCount);
        return retResp;
    }

    /**
     * 新增或更新Article
     *
     * @param changedArticle 需要变更的Article
     * @return 是否成功
     */
    @PostMapping
    public ApiResp<Boolean> insertOrUpdateArticle(@RequestBody ChangedArticle changedArticle) {
        boolean isInsert = changedArticle.getId() == 0;
        Article articleEntity = isInsert ? new Article() : articleService.selectById(changedArticle.getId());
        changedArticle.copyTo(articleEntity);
        SysUser curUser = SecurityUtil.getCurrentUser();
        if (curUser != null) {
            articleEntity.setModifyUserId(sysUserService.getIdByUsername(curUser.getUsername()));
        }
        boolean result = false;
        if (isInsert) {
            articleEntity.setCreateTime(articleEntity.getModifyTime());
            articleEntity.setCreateUserId(articleEntity.getModifyUserId());
            result = articleService.insert(articleEntity) > 0;
        } else {
            result = articleService.update(articleEntity) > 0;
        }
        if (result) {
            ApiResp<Boolean> retResp = new ApiResp<Boolean>().success();
            retResp.setData(true);
            return retResp;
        } else {
            ApiResp<Boolean> retResp = new ApiResp<Boolean>();
            retResp.setCode(501);
            retResp.setMessage("保存Article信息到数据库失败");
            retResp.setData(false);
            return retResp;
        }
    }

    /**
     * 删除Article
     *
     * @param id ArticleId
     * @return 是否删除成功
     */
    @PostMapping
    public ApiResp<Boolean> deleteArticle(@Param("articleId") int id) {
        boolean result = articleService.remove(id) > 0;
        if (result) {
            ApiResp<Boolean> retResp = new ApiResp<Boolean>().success();
            retResp.setData(true);
            return retResp;
        } else {
            ApiResp<Boolean> retResp = new ApiResp<Boolean>();
            retResp.setCode(501);
            retResp.setMessage("数据库删除Article信息失败");
            retResp.setData(false);
            return retResp;
        }
    }

}

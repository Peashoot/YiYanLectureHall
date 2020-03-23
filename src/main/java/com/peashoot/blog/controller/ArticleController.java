package com.peashoot.blog.controller;

import com.peashoot.blog.batis.entity.ArticleDO;
import com.peashoot.blog.batis.entity.SysUserDO;
import com.peashoot.blog.batis.service.ArticleService;
import com.peashoot.blog.batis.service.SysUserService;
import com.peashoot.blog.context.request.article.ArticleSearchDTO;
import com.peashoot.blog.context.request.article.ChangedArticleDTO;
import com.peashoot.blog.context.response.ApiResp;
import com.peashoot.blog.context.response.article.ArticleIntroductionDTO;
import com.peashoot.blog.context.response.article.ArticlesCollectionDTO;
import com.peashoot.blog.util.SecurityUtil;
import com.peashoot.blog.util.StringUtils;
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
    public ApiResp<ArticlesCollectionDTO> getArticles(@RequestBody ArticleSearchDTO searchCondition) {
        List<ArticleDO> matchedList = articleService.listPagedArticles(searchCondition.getPageSize(), searchCondition.getPageIndex(), searchCondition.getAuthorLike(), searchCondition.getKeywordLike(), searchCondition.getTitleLike());
        int totalCount = articleService.countTotalRecords(searchCondition.getAuthorLike(), searchCondition.getKeywordLike(), searchCondition.getTitleLike());
        ApiResp<ArticlesCollectionDTO> retResp = new ApiResp<ArticlesCollectionDTO>().success();
        ArticlesCollectionDTO data = new ArticlesCollectionDTO();
        data.setArticleList(matchedList.stream().map(a -> ArticleIntroductionDTO.createArticlesInfo(a)).collect(Collectors.toList()));
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
    public ApiResp<Boolean> insertOrUpdateArticle(@RequestBody ChangedArticleDTO changedArticle) {
        boolean isInsert = StringUtils.isNullOrEmpty(changedArticle.getId());
        ArticleDO articleEntity = isInsert ? new ArticleDO() : articleService.selectById(changedArticle.getId());
        changedArticle.copyTo(articleEntity);
        SysUserDO curUser = SecurityUtil.getCurrentUser();
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
    public ApiResp<Boolean> deleteArticle(@Param("articleId") String id) {
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

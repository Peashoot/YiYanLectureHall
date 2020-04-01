package com.peashoot.blog.controller;

import com.peashoot.blog.aspect.annotation.ErrorRecord;
import com.peashoot.blog.aspect.annotation.VisitLimit;
import com.peashoot.blog.batis.entity.ArticleDO;
import com.peashoot.blog.batis.entity.SysUserDO;
import com.peashoot.blog.batis.entity.VisitActionEnum;
import com.peashoot.blog.batis.entity.OperateRecordDO;
import com.peashoot.blog.batis.service.ArticleService;
import com.peashoot.blog.batis.service.SysUserService;
import com.peashoot.blog.batis.service.OperateRecordService;
import com.peashoot.blog.context.request.article.ArticleAgreeDTO;
import com.peashoot.blog.context.request.article.ArticleSearchDTO;
import com.peashoot.blog.context.request.article.ChangedArticleDTO;
import com.peashoot.blog.context.response.ApiResp;
import com.peashoot.blog.context.response.article.ArticleIntroductionDTO;
import com.peashoot.blog.context.response.article.ArticlesCollectionDTO;
import com.peashoot.blog.util.Constant;
import com.peashoot.blog.util.SecurityUtil;
import com.peashoot.blog.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 文章信息操作接口
 *
 * @author peashoot
 */
@RestController
@Api(tags = "文章信息操作接口")
@RequestMapping(path = "article")
@EnableAsync
@ErrorRecord
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
     * 访客操作记录操作类
     */
    @Autowired
    private OperateRecordService visitRecordService;

    /**
     * 根据查询条件获取符合条件的Article
     *
     * @param apiReq 查询条件
     * @return 符合条件的Article
     */
    @RequestMapping(path = "list")
    @ApiOperation("获取符合查询条件的文章")
    public ApiResp<ArticlesCollectionDTO> getArticles(@RequestBody @Valid ArticleSearchDTO apiReq, BindingResult bindingResult) {
        List<ArticleDO> matchedList = articleService.listPagedArticles(apiReq.getPageSize(), apiReq.getPageIndex(), apiReq.getAuthorLike(), apiReq.getKeywordLike(), apiReq.getTitleLike());
        int totalCount = articleService.countTotalRecords(apiReq.getAuthorLike(), apiReq.getKeywordLike(), apiReq.getTitleLike());
        ApiResp<ArticlesCollectionDTO> retResp = new ApiResp<ArticlesCollectionDTO>().success();
        ArticlesCollectionDTO data = new ArticlesCollectionDTO();
        data.setArticleList(matchedList.stream().map(a -> ArticleIntroductionDTO.createArticlesInfo(a)).collect(Collectors.toList()));
        data.setPageSize(apiReq.getPageSize());
        data.setPageIndex(apiReq.getPageIndex());
        data.setTotalRecordsCount(totalCount);
        return retResp;
    }

    /**
     * 新增或更新Article
     *
     * @param apiReq 需要变更的Article
     * @return 是否成功
     */
    @PostMapping(path = "modify")
    @ApiOperation("新增或更新文章")
    @PreAuthorize("hasRole('writer')")
    public ApiResp<Boolean> insertOrUpdateArticle(@RequestBody ChangedArticleDTO apiReq) {
        ApiResp<Boolean> retResp = new ApiResp<>();
        retResp.setCode(501);
        retResp.setMessage("Failure to save article.");
        boolean isInsert = StringUtils.isNullOrEmpty(apiReq.getId());
        ArticleDO articleEntity = isInsert ? new ArticleDO() : articleService.selectById(apiReq.getId());
        apiReq.copyTo(articleEntity);
        SysUserDO curUser = SecurityUtil.getCurrentUser();
        if (curUser == null) {
            return retResp;
        }
        articleEntity.setModifyUserId(sysUserService.getIdByUsername(curUser.getUsername()));
        boolean result;
        if (isInsert) {
            articleEntity.setCreateTime(articleEntity.getModifyTime());
            articleEntity.setCreateUserId(articleEntity.getModifyUserId());
            visitRecordService.insertNewRecordAsync(articleEntity.getCreateUserId(), apiReq.getId(), VisitActionEnum.CREATE_ARTICLE, new Date(), "Add an article：" + apiReq.getTitle());
            result = articleService.insert(articleEntity) > 0;
        } else {
            visitRecordService.insertNewRecordAsync(articleEntity.getModifyUserId(), apiReq.getId(), VisitActionEnum.UPDATE_ARTICLE, new Date(), "Modify an article：" + apiReq.getTitle());
            result = articleService.update(articleEntity) > 0;
        }
        if (result) {
            retResp.success().setData(true);
        }
        return retResp;
    }

    /**
     * 删除Article
     *
     * @param id ArticleId
     * @return 是否删除成功
     */
    @PostMapping(path = "delete")
    @ApiOperation("删除文章")
    @PreAuthorize("hasRole('writer')")
    public ApiResp<Boolean> deleteArticle(@RequestParam("articleId") String id) {
        ApiResp<Boolean> retResp = new ApiResp<>();
        retResp.setCode(501);
        retResp.setMessage("Failure to remove article.");
        SysUserDO curUser = SecurityUtil.getCurrentUser();
        if (curUser == null) {
            return retResp;
        }
        visitRecordService.insertNewRecordAsync(sysUserService.getIdByUsername(curUser.getUsername()), id, VisitActionEnum.DELETE_ARTICLE, new Date(), "Delete an article, it's id is " + id);
        // 逻辑删除设置IsDelete为true
        boolean result = articleService.remove(id) > 0;
        if (result) {
            retResp.success().setData(true);
        }
        return retResp;
    }

    /**
     * 对文章进行点赞或反对操作
     *
     * @param apiReq 评论情况
     * @return 是否成功
     */
    @PostMapping(path = "reviews")
    @ApiOperation("文章点赞或反对")
    @VisitLimit(value = 5)
    public ApiResp<Boolean> agreeOrDisagreeArticle(@RequestBody ArticleAgreeDTO apiReq) {
        ApiResp<Boolean> resp = new ApiResp<>();
        resp.setCode(406);
        resp.setMessage("Failure to action or disagree article");
        OperateRecordDO visitRecordDO = visitRecordService.selectLastRecordByVisitorIdAndArticleId(apiReq.getVisitorId(), apiReq.getArticleId());
        int agree = 0, disagree = 0;
        switch (apiReq.getAction()) {
            case AGREE_ARTICLE:
                // 点赞时，需要满足访客操作清零的条件
                if (visitRecordDO == null || visitRecordDO.getAction() == VisitActionEnum.CANCEL_AGREE_ARTICLE
                        || visitRecordDO.getAction() == VisitActionEnum.CANCEL_DISAGREE_ARTICLE) {
                    agree = 1;
                }
                break;
            // 取消点赞，需要满足访客已点赞的条件
            case CANCEL_AGREE_ARTICLE:
                if (visitRecordDO != null && visitRecordDO.getAction() == VisitActionEnum.AGREE_ARTICLE) {
                    agree = -1;
                }
                break;
            // 反对时，需要满足访客操作清零的条件
            case DISAGREE_ARTICLE:
                if (visitRecordDO == null || visitRecordDO.getAction() == VisitActionEnum.CANCEL_AGREE_ARTICLE
                        || visitRecordDO.getAction() == VisitActionEnum.CANCEL_DISAGREE_ARTICLE) {
                    disagree = 1;
                }
                break;
            // 取消反对，需要满足访客已反对的条件
            case CANCEL_DISAGREE_ARTICLE:
                if (visitRecordDO != null && visitRecordDO.getAction() == VisitActionEnum.DISAGREE_ARTICLE) {
                    disagree = -1;
                }
                break;
            default:
                break;
        }
        if (agree + disagree == 0) {
            return resp;
        }
        // 新增访客操作记录并修改评论点赞反对数
        visitRecordService.insertNewRecordAsync(apiReq.getVisitorId(), apiReq.getArticleId(), apiReq.getAction(), new Date(), "");
        boolean result = articleService.updateSupportAndDisagreeState(apiReq.getArticleId(), agree, disagree);
        resp.success().setData(result);
        return resp;
    }
}

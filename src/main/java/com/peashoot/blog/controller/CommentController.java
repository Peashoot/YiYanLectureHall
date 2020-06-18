package com.peashoot.blog.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Objects;
import com.peashoot.blog.aspect.annotation.ErrorRecord;
import com.peashoot.blog.aspect.annotation.VisitTimesLimit;
import com.peashoot.blog.batis.entity.ArticleDO;
import com.peashoot.blog.batis.entity.CommentDO;
import com.peashoot.blog.batis.enums.VisitActionEnum;
import com.peashoot.blog.batis.entity.OperateRecordDO;
import com.peashoot.blog.batis.entity.VisitorDO;
import com.peashoot.blog.batis.service.ArticleService;
import com.peashoot.blog.batis.service.CommentService;
import com.peashoot.blog.batis.service.OperateRecordService;
import com.peashoot.blog.batis.service.VisitorService;
import com.peashoot.blog.context.request.comment.CommentAgreeDTO;
import com.peashoot.blog.context.request.comment.PageCommentDTO;
import com.peashoot.blog.context.request.comment.VisitorWithCommentDTO;
import com.peashoot.blog.context.response.ApiResp;
import com.peashoot.blog.context.response.comment.ArticleCommentDTO;
import com.peashoot.blog.context.response.comment.PagedCommentsDTO;
import com.peashoot.blog.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * 评论管理相关接口
 *
 * @author peashoot
 */
@RestController
@Api(tags = "评论管理相关接口")
@RequestMapping(path = "comment")
@EnableAsync
@ErrorRecord
public class CommentController {
    /**
     * 评论操作类
     */
    private final CommentService commentService;
    /**
     * 访客操作类
     */
    private final VisitorService visitorService;
    /**
     * 访客操作记录操作类
     */
    private final OperateRecordService visitRecordService;
    /**
     * 文章操作类
     */
    private final ArticleService articleService;

    public CommentController(CommentService commentService, VisitorService visitorService, OperateRecordService visitRecordService, ArticleService articleService) {
        this.commentService = commentService;
        this.visitorService = visitorService;
        this.visitRecordService = visitRecordService;
        this.articleService = articleService;
    }

    /**
     * 新增访客评论
     *
     * @param apiReq 访客信息和评论信息
     * @return 是否成功保存
     */
    @PostMapping(path = "insert")
    @ApiOperation("新增访客评论")
    @VisitTimesLimit(value = 5)
    public ApiResp<Boolean> insertCommentOfVisitor(@RequestBody @Validated VisitorWithCommentDTO apiReq) {
        ApiResp<Boolean> resp = new ApiResp<>();
        resp.setCode(ApiResp.PROCESS_ERROR);
        resp.setMessage("Failure comment");
        ArticleDO articleDO = articleService.selectById(apiReq.getArticleId());
        if (articleDO == null) {
            resp.setCode(ApiResp.NO_RECORD_MATCH);
            resp.setMessage("No matched article");
            return resp;
        }
        VisitorDO visitorDO = visitorService.selectByVisitorName(apiReq.getVisitor());
        if (visitorDO == null) {
            resp.setCode(ApiResp.NO_RECORD_MATCH);
            resp.setMessage("No matched visitor");
            return resp;
        }
        CommentDO comment = apiReq.createCommentEntity(visitorDO.getId());
        visitRecordService.insertNewRecordAsync(visitorDO.getId(), apiReq.getArticleId(), apiReq.getVisitorIp(),
                VisitActionEnum.COMMENT, new Date(), "Comment to article：" + apiReq.getArticleId());
        if (commentService.insert(comment) == 0) {
            return resp;
        }
        resp.success().setData(true);
        return resp;
    }

    /**
     * 分页获取访客评论信息
     *
     * @param apiReq 分页查询条件
     * @return 查询结果
     */
    @PostMapping(path = "paging")
    @ApiOperation("分页获取访客评论")
    public ApiResp<PagedCommentsDTO> getPagedComments(@RequestBody PageCommentDTO apiReq) {
        ApiResp<PagedCommentsDTO> resp = new ApiResp<>();
        resp.setCode(ApiResp.PROCESS_ERROR);
        resp.setMessage("Failure query");
        List<CommentDO> matchedCommentRecords = commentService.listPagedComments(apiReq.getPageSize(), apiReq.getPageIndex(), apiReq.getArticleId());
        int totalMatchedRecordsCount = commentService.countTotalRecords(apiReq.getArticleId());
        PagedCommentsDTO pagedComments = new PagedCommentsDTO();
        pagedComments.setPageSize(apiReq.getPageSize());
        pagedComments.setPageIndex(apiReq.getPageIndex());
        pagedComments.setTotalCount(totalMatchedRecordsCount);
        List<ArticleCommentDTO> articleCommentList = new ArrayList<>();
        for (CommentDO comment : matchedCommentRecords) {
            ArticleCommentDTO articleComment = generateArticleComment(comment, articleCommentList);
            articleCommentList.add(articleComment);
        }
        pagedComments.setComments(articleCommentList);
        resp.success().setData(pagedComments);
        return resp;
    }

    /**
     * 生成文章评论返回对象
     *
     * @param comment    评论实体
     * @param loadedList 已加载的列表
     * @return 加载实体
     */
    private ArticleCommentDTO generateArticleComment(CommentDO comment, List<ArticleCommentDTO> loadedList) {
        ArticleCommentDTO commentTo = null;
        if (comment.getCommentTo() > 0) {
            Stream<ArticleCommentDTO> matchedComments = loadedList.stream().filter(item -> Objects.equal(item.getId(), comment.getCommentTo()));
            commentTo = matchedComments.findFirst().orElse(null);
            if (commentTo == null) {
                CommentDO commentToComment = commentService.selectById(comment.getCommentTo());
                if (commentToComment != null) {
                    commentTo = generateArticleComment(comment, loadedList);
                }
            }
        }
        String visitorName = comment.getAnonymous() || StringUtils.isNullOrEmpty(comment.getSysUserNickname()) ? comment.getVisitorName() : comment.getSysUserNickname();
        return ArticleCommentDTO.generateArticleComment(comment, commentTo, visitorName);
    }

    /**
     * 访客对评论进行点赞，返回或取消操作
     *
     * @param apiReq 访客操作信息
     * @return 是否成功
     */
    @PostMapping(path = "reviews")
    @ApiOperation("点赞或反对评论")
    @VisitTimesLimit(value = 5)
    public ApiResp<Boolean> agreeOrDisagreeComment(@RequestBody CommentAgreeDTO apiReq) {
        ApiResp<Boolean> resp = new ApiResp<>();
        resp.setCode(ApiResp.PROCESS_ERROR);
        resp.setMessage("Failure to action or disagree comment");
        OperateRecordDO visitRecordDO = visitRecordService.selectLastRecordByVisitorIdAndCommentId(apiReq.getVisitorId(), apiReq.getCommentId());
        int agree = 0, disagree = 0;
        switch (apiReq.getAction()) {
            case AGREE_COMMENT:
                // 点赞时，需要满足访客操作清零的条件
                if (visitRecordDO == null || visitRecordDO.getAction() == VisitActionEnum.CANCEL_AGREE_COMMENT
                        || visitRecordDO.getAction() == VisitActionEnum.CANCEL_DISAGREE_COMMENT) {
                    agree = 1;
                }
                break;
            // 取消点赞，需要满足访客已点赞的条件
            case CANCEL_AGREE_COMMENT:
                if (visitRecordDO != null && visitRecordDO.getAction() == VisitActionEnum.AGREE_COMMENT) {
                    agree = -1;
                }
                break;
            // 反对时，需要满足访客操作清零的条件
            case DISAGREE_COMMENT:
                if (visitRecordDO == null || visitRecordDO.getAction() == VisitActionEnum.CANCEL_AGREE_COMMENT
                        || visitRecordDO.getAction() == VisitActionEnum.CANCEL_DISAGREE_COMMENT) {
                    disagree = 1;
                }
                break;
            // 取消反对，需要满足访客已反对的条件
            case CANCEL_DISAGREE_COMMENT:
                if (visitRecordDO != null && visitRecordDO.getAction() == VisitActionEnum.DISAGREE_COMMENT) {
                    disagree = -1;
                }
                break;
            default:
                break;
        }
        if (agree + disagree == 0) {
            resp.setCode(ApiResp.BAD_REQUEST);
            resp.setMessage("Repeated operation.");
            return resp;
        }
        // 新增访客操作记录并修改评论点赞反对数
        visitRecordService.insertNewRecordAsync(apiReq.getVisitorId(), apiReq.getCommentId().toString(), apiReq.getVisitorIp(), apiReq.getAction(), new Date(), "");
        boolean result = commentService.updateSupportAndDisagreeState(apiReq.getCommentId(), agree, disagree);
        if (!result) {
            return resp;
        }
        resp.success().setData(true);
        return resp;
    }
}

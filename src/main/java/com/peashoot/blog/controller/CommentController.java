package com.peashoot.blog.controller;

import com.peashoot.blog.aspect.annotation.ErrorRecord;
import com.peashoot.blog.aspect.annotation.VisitLimit;
import com.peashoot.blog.batis.entity.CommentDO;
import com.peashoot.blog.batis.enums.VisitActionEnum;
import com.peashoot.blog.batis.entity.OperateRecordDO;
import com.peashoot.blog.batis.entity.VisitorDO;
import com.peashoot.blog.batis.service.CommentService;
import com.peashoot.blog.batis.service.OperateRecordService;
import com.peashoot.blog.batis.service.VisitorService;
import com.peashoot.blog.context.request.comment.CommentAgreeDTO;
import com.peashoot.blog.context.request.comment.PageCommentDTO;
import com.peashoot.blog.context.request.comment.VisitorWithCommentDTO;
import com.peashoot.blog.context.response.ApiResp;
import com.peashoot.blog.context.response.comment.ArticleCommentDTO;
import com.peashoot.blog.context.response.comment.PagedCommentsDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
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
    @Autowired
    private CommentService commentService;
    /**
     * 访客操作类
     */
    @Autowired
    private VisitorService visitorService;
    /**
     * 访客操作记录操作类
     */
    @Autowired
    private OperateRecordService visitRecordService;

    /**
     * 新增访客评论
     *
     * @param apiReq 访客信息和评论信息
     * @return 是否成功保存
     */
    @PostMapping(path = "insert")
    @ApiOperation("新增访客评论")
    @VisitLimit(value = 5)
    public ApiResp<Boolean> insertCommentOfVisitor(@RequestBody VisitorWithCommentDTO apiReq) {
        ApiResp<Boolean> resp = new ApiResp<>();
        CommentDO comment = apiReq.createCommentEntity();
        visitRecordService.insertNewRecordAsync(apiReq.getVisitorId(), apiReq.getArticleId(), apiReq.getVisitorIP(), VisitActionEnum.COMMENT, new Date(), "Comment to article：" + apiReq.getArticleId());
        resp.success().setData(commentService.insert(comment) > 0);
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
        List<CommentDO> matchedCommentRecords = commentService.listPagedComments(apiReq.getPageSize(), apiReq.getPageIndex(), apiReq.getArticleId());
        int totalMatchedRecordsCount = commentService.countTotalRecords(apiReq.getArticleId());
        ApiResp<PagedCommentsDTO> resp = new ApiResp<>();
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
        String visitorName = "ghost";
        if (comment.getId() > 0) {
            Stream<ArticleCommentDTO> matchedComments = loadedList.stream().filter(item -> item.getId() == comment.getId());
            if (matchedComments.count() > 0) {
                commentTo = matchedComments.findFirst().get();
            } else {
                CommentDO commentToComment = commentService.selectById(comment.getId());
                if (commentToComment != null) {
                    commentTo = generateArticleComment(comment, loadedList);
                }
            }
        }
        if (comment.getVisitorId() > 0) {
            VisitorDO visitor = visitorService.selectById(comment.getVisitorId());
            if (visitor != null) {
                visitorName = comment.getAnonymous() ? visitor.getVisitor() : visitor.getSysUserNickName();
            }
        }
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
    @VisitLimit(value = 5)
    private ApiResp<Boolean> agreeOrDisagreeComment(@RequestBody CommentAgreeDTO apiReq) {
        ApiResp<Boolean> resp = new ApiResp<>();
        resp.setCode(406);
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
            return resp;
        }
        // 新增访客操作记录并修改评论点赞反对数
        visitRecordService.insertNewRecordAsync(apiReq.getVisitorId(), apiReq.getCommentId().toString(), apiReq.getVisitorIP(), apiReq.getAction(), new Date(), "");
        boolean result = commentService.updateSupportAndDisagreeState(apiReq.getCommentId(), agree, disagree);
        resp.success().setData(result);
        return resp;
    }
}

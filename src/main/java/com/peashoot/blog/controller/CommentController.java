package com.peashoot.blog.controller;

import com.peashoot.blog.batis.entity.CommentDO;
import com.peashoot.blog.batis.entity.VisitActionEnum;
import com.peashoot.blog.batis.entity.VisitRecordDO;
import com.peashoot.blog.batis.entity.VisitorDO;
import com.peashoot.blog.batis.service.CommentService;
import com.peashoot.blog.batis.service.VisitRecordService;
import com.peashoot.blog.batis.service.VisitorService;
import com.peashoot.blog.context.request.comment.CommentAgreeDTO;
import com.peashoot.blog.context.request.comment.PageCommentDTO;
import com.peashoot.blog.context.request.comment.VisitorWithCommentDTO;
import com.peashoot.blog.context.response.ApiResp;
import com.peashoot.blog.context.response.comment.ArticleCommentDTO;
import com.peashoot.blog.context.response.comment.PagedCommentsDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private VisitorService visitorService;
    @Autowired
    private VisitRecordService visitRecordService;

    @PostMapping(path = "insert")
    @ApiOperation("新增访客评论")
    public ApiResp<Boolean> insertCommentOfVisitor(@RequestBody VisitorWithCommentDTO visitorWithComment) {
        ApiResp<Boolean> resp = new ApiResp<>();
        CommentDO comment = visitorWithComment.createCommentEntity();
        resp.success().setData(commentService.insert(comment) > 0);
        return resp;
    }

    @PostMapping(path = "paging")
    @ApiOperation("分页获取访客评论")
    public ApiResp<PagedCommentsDTO> getPagedComments(@RequestBody PageCommentDTO pageQuery) {
        List<CommentDO> matchedCommentRecords = commentService.listPagedComments(pageQuery.getPageSize(), pageQuery.getPageIndex(), pageQuery.getArticleId());
        int totalMatchedRecordsCount = commentService.countTotalRecords(pageQuery.getArticleId());
        ApiResp<PagedCommentsDTO> resp = new ApiResp<>();
        PagedCommentsDTO pagedComments = new PagedCommentsDTO();
        pagedComments.setPageSize(pageQuery.getPageSize());
        pagedComments.setPageIndex(pageQuery.getPageIndex());
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
                visitorName = comment.isAnonymous() ? visitor.getVisitor() : visitor.getSysUserNickName();
            }
        }
        return ArticleCommentDTO.generateArticleComment(comment, commentTo, visitorName);
    }

    /**
     * 访客对评论进行点赞，返回或取消操作
     *
     * @param commentAgree 访客操作信息
     * @return 是否成功
     */
    private ApiResp<Boolean> agreeOrDisagreeComment(@RequestBody CommentAgreeDTO commentAgree) {
        ApiResp<Boolean> resp = new ApiResp<>();
        resp.setCode(406);
        resp.setMessage("Failure to action or disagree comment");
        VisitRecordDO visitRecordDO = visitRecordService.selectLastRecordByVisitorIdAndCommentId(commentAgree.getVisitorId(), commentAgree.getCommentId());
        int agree = 0, disagree = 0;
        switch (commentAgree.getAction()) {
            case AGREE_COMMENT:
                // 点赞时，需要满足访客操作清零的条件
                if (visitRecordDO == null || visitRecordDO.getAction() == VisitActionEnum.CANCEL_AGREE_COMMENT
                        || visitRecordDO.getAction() == VisitActionEnum.CANCEL_DISAGREE_ARTICLE) {
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
                        || visitRecordDO.getAction() == VisitActionEnum.CANCEL_DISAGREE_ARTICLE) {
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
        boolean result = visitRecordService.insertNewRecord(commentAgree.getVisitorId(), commentAgree.getCommentId(), commentAgree.getAction(), new Date(), "")
                && commentService.updateSupportAndDisagreeState(commentAgree.getCommentId(), agree, disagree);
        resp.success().setData(result);
        return resp;
    }
}

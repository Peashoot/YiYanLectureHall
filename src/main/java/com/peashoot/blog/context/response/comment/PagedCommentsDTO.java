package com.peashoot.blog.context.response.comment;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = false)
// @JSONType(serialzeFeatures = {SerializerFeature.DisableCircularReferenceDetect})
public class PagedCommentsDTO {
    /**
     * 分页大小
     */
    private int pageSize;
    /**
     * 当前页码
     */
    private int pageIndex;
    /**
     * 总记录数
     */
    private int totalCount;
    /**
     * 评论
     */
    private List<ArticleCommentDTO> comments;
}

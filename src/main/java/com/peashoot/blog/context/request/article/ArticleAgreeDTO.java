package com.peashoot.blog.context.request.article;

import com.alibaba.fastjson.annotation.JSONField;
import com.peashoot.blog.batis.enums.VisitActionEnum;
import com.peashoot.blog.context.request.BaseApiReq;
import com.peashoot.blog.util.json.deserializer.EnumCodec;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = false)
public class ArticleAgreeDTO extends BaseApiReq {
    /**
     * 访客ID
     */
    private Long visitorId;
    /**
     * 文章ID
     */
    private String articleId;
    /**
     * 是否同意
     */
    @JSONField(deserializeUsing = EnumCodec.class)
    private VisitActionEnum action;
}

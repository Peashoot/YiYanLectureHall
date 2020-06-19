package com.peashoot.blog.context.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.peashoot.blog.batis.entity.VisitorDO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseApiReqWithVisitor extends BaseApiReq {
    /**
     * 访客ID
     */
    private String visitor;
    /**
     * 访客信息
     */
    @JSONField(serialize = false, deserialize = false)
    private VisitorDO visitorDO;
}

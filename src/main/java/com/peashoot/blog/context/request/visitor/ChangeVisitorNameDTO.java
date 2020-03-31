package com.peashoot.blog.context.request.visitor;

import com.peashoot.blog.context.request.BaseApiReq;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = false)
public class ChangeVisitorNameDTO extends BaseApiReq {
    /**
     * 访客id
     */
    private Long visitorId;
    /**
     * 旧的访客名称
     */
    private String oldVisitorName;
    /**
     * 新的访客名称
     */
    private String newVisitorName;
}

package com.peashoot.blog.context.response.visitor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisitorIDAndNamesDTO {
    /**
     * 访客名称
     */
    private String visitorName;
    /**
     * 系统用户名称
     */
    private String sysUserNickname;
    /**
     * 访客ID
     */
    private long visitorId;
}

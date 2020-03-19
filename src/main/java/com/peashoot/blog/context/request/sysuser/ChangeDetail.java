package com.peashoot.blog.context.request.sysuser;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangeDetail extends SysUserDetail {
    /**
     * 用户id
     */
    private int id;
}

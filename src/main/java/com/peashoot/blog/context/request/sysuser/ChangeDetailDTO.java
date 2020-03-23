package com.peashoot.blog.context.request.sysuser;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangeDetailDTO extends SysUserDetailDTO {
    /**
     * 用户id
     */
    private Integer id;
}

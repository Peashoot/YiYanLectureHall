package com.peashoot.blog.context.request.sysuser;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true, includeFieldNames = false)
public class ChangeDetailDTO extends SysUserDetailDTO {
    /**
     * 用户id
     */
    private Integer id;
}

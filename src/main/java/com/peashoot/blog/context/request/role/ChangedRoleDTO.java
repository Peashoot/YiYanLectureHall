package com.peashoot.blog.context.request.role;

import com.alibaba.fastjson.annotation.JSONField;
import com.peashoot.blog.batis.enums.PermissionEnum;
import com.peashoot.blog.context.request.BaseApiReq;
import com.peashoot.blog.util.json.deserializer.EnumArrayCodec;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangedRoleDTO extends BaseApiReq {
    /**
     * 角色id
     */
    private Integer roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色权限
     */
    @JSONField(deserializeUsing = EnumArrayCodec.class, serializeUsing = EnumArrayCodec.class)
    private PermissionEnum[] permissions;
}

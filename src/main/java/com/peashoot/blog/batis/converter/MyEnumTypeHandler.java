package com.peashoot.blog.batis.converter;

import com.peashoot.blog.batis.enums.*;
import com.peashoot.blog.batis.enums.base.BaseEnum;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(value = {FileTypeEnum.class, VisitActionEnum.class, OperatorTypeEnum.class, GenderEnum.class, PermissionEnum.class})
public class MyEnumTypeHandler<E extends Enum<E> & BaseEnum> extends BaseEnumTypeHandler<E> {
    /**
     * 设置配置文件设置的转换类以及枚举类内容，供其他方法更便捷高效的实现
     *
     * @param type 配置文件中设置的转换类
     */
    public MyEnumTypeHandler(Class<E> type) {
        super(type);
    }
}
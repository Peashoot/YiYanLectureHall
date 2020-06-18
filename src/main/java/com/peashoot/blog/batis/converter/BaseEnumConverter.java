package com.peashoot.blog.batis.converter;

import com.peashoot.blog.batis.enums.FileTypeEnum;
import com.peashoot.blog.batis.enums.base.BaseEnum;
import org.springframework.core.convert.converter.Converter;

import java.util.Objects;

public class BaseEnumConverter<E extends Enum<E> & BaseEnum, T> implements Converter<T, E> {
    /**
     * 枚举的每个子类枚
     */
    private BaseEnum[] enums;

    /**
     * 设置配置文件设置的转换类以及枚举类内容，供其他方法更便捷高效的实现
     *
     */
    public BaseEnumConverter(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.enums = type.getEnumConstants();
        if (this.enums == null) {
            throw new IllegalArgumentException(type.getSimpleName()
                    + " does not represent an enum type.");
        }
    }

    /**
     * 枚举类型转换，由于构造函数获取了枚举的子类 enums，让遍历更加高效快捷，
     * <p>
     * 我将取出来的值 全部转换成字符串 进行比较，
     *
     * @param source 数据库中存储的自定义value属性
     * @return value 对应的枚举类
     */
    private E locateEnumStatus(T source) {
        for (BaseEnum e : enums) {
            if (Objects.equals(e.getValue(), source)) {
                return (E)e;
            }
        }
        throw new IllegalArgumentException("未知的枚举类型：" + source + ",请核对"
                + FileTypeEnum.class.getSimpleName());
    }

    @Override
    public E convert(T source) {
        return locateEnumStatus(source);
    }
}

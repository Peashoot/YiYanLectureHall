package com.peashoot.blog.batis.converter.factory;

import com.peashoot.blog.batis.converter.BaseEnumConverter;
import com.peashoot.blog.batis.enums.FileTypeEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.Map;
import java.util.WeakHashMap;

public class FileTypeEnumConverterFactory implements ConverterFactory<String, FileTypeEnum> {
    private static final Map<Class, Converter> INTERNAL_CONVERTER_MAP = new WeakHashMap<>();

    @Override
    public <T extends FileTypeEnum> Converter<String, T> getConverter(Class<T> targetType) {
        Converter result = INTERNAL_CONVERTER_MAP.get(targetType);
        if (result == null) {
            result = new BaseEnumConverter(targetType);
            INTERNAL_CONVERTER_MAP.put(targetType, result);
        }
        return result;
    }
}

package com.peashoot.blog.util.json.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.MiscCodec;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.peashoot.blog.batis.enums.base.BaseEnum;
import com.peashoot.blog.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.*;

public class EnumArrayCodec implements ObjectSerializer, ObjectDeserializer {
    public final static EnumArrayCodec instance                   = new EnumArrayCodec();
    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String value = parser.parse(fieldName).toString();
        if (!(type instanceof Class)) {
            throw new IllegalArgumentException("Input parameter is not match enum array type");
        }
        Class temp = (Class) type;
        if (!temp.isArray() || !BaseEnum.class.isAssignableFrom(temp.getComponentType())) {
            throw new IllegalArgumentException("Input parameter is not match enum array type");
        }
        if (StringUtils.NULL.equalsIgnoreCase(value)) {
            return null;
        }
        value = value.substring(1, value.length() - 1);
        String[] valuePairs = value.split(",");
        List list = Collections.checkedList(new ArrayList(), temp.getComponentType());
        for (String childValue : valuePairs) {
            for (BaseEnum e : (BaseEnum[]) temp.getComponentType().getEnumConstants()) {
                String afterArim = childValue.substring(1, childValue.length() - 1);
                if (Objects.toString(e.getValue()).equals(afterArim)) {
                    list.add(e);
                }
            }
        }
        return (T)list.toArray(createEmptyArray(temp.getComponentType()));
    }

    @SuppressWarnings("unchecked")
    private <E> E[] createEmptyArray(Class<E> tElement) {
        return (E[]) Array.newInstance(tElement, 0);
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        BaseEnum[] value = (BaseEnum[]) object;
        if (value == null) {
            out.writeNull();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (BaseEnum e: value) {
            stringBuilder.append('"').append(e.getValue()).append('"').append(',');
        }
        // 如果添加了enum元素，则去掉最后面的逗号
        if (stringBuilder.length() > 1) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append("]");
        out.write(stringBuilder.toString());
    }
}

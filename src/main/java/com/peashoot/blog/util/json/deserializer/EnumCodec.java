package com.peashoot.blog.util.json.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.peashoot.blog.batis.enums.base.BaseEnum;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

public class EnumCodec implements ObjectSerializer, ObjectDeserializer {

    public final static EnumCodec instance                   = new EnumCodec();
    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String value = parser.parse(fieldName).toString();
        if (!(type instanceof Class)) {
            throw new IllegalArgumentException("Input parameter is not match enum type");
        }
        Class temp = (Class) type;
        if (!temp.isEnum() || !BaseEnum.class.isAssignableFrom(temp)) {
            throw new IllegalArgumentException("Input parameter is not match enum type");
        }
        Class<T> clazz = (Class<T>) temp;
        for (T e : clazz.getEnumConstants()) {
            if (Objects.toString(((BaseEnum) e).getValue()).equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Unknown type：" + type.getTypeName());
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        BaseEnum value = (BaseEnum) object;
        if (value == null) {
            out.writeNull(SerializerFeature.WriteNullStringAsEmpty);
            return;
        }
        out.write(Objects.toString(value.getValue()));
    }
}

package com.peashoot.blog.batis.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.peashoot.blog.batis.enums.PermissionEnum;
import com.peashoot.blog.util.json.deserializer.EnumArrayCodec;
import com.peashoot.blog.util.json.registry.MyFastJsonParseConfig;
import com.peashoot.blog.util.json.registry.MyFastJsonSerializeConfig;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.lang.reflect.ParameterizedType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(PermissionEnum[].class)
public class JsonTypeHandler<T> extends BaseTypeHandler<T> {
    public JsonTypeHandler(Class<T> tClass) {
        genericClass = tClass;
        parserConfig = MyFastJsonParseConfig.instance;
        serializeConfig = MyFastJsonSerializeConfig.instance;
    }

    /**
     * json解析器配置
     */
    private ParserConfig parserConfig;
    /**
     * 通用泛型类型
     */
    private Class<T> genericClass;
    /**
     * json序列化器配置
     */
    private SerializeConfig serializeConfig;

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setString(i, JSON.toJSONString(parameter, serializeConfig));
        } else {
            ps.setObject(i, JSON.toJSONString(parameter, serializeConfig), jdbcType.TYPE_CODE);
        }
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String i = rs.getString(columnName);
        if (rs.wasNull()) {
            return null;
        } else {
            return JSON.parseObject(i, genericClass, parserConfig);
        }
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String i = rs.getString(columnIndex);
        if (rs.wasNull()) {
            return null;
        } else {
            return JSON.parseObject(i, genericClass, parserConfig);
        }
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String i = cs.getString(columnIndex);
        if (cs.wasNull()) {
            return null;
        } else {
            return JSON.parseObject(i, genericClass, parserConfig);
        }
    }
}

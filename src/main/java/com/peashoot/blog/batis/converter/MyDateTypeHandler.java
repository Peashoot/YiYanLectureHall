package com.peashoot.blog.batis.converter;

import com.peashoot.blog.batis.enums.FileTypeEnum;
import com.peashoot.blog.batis.enums.OperatorTypeEnum;
import com.peashoot.blog.batis.enums.VisitActionEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

@MappedTypes(value = {Date.class})
public class MyDateTypeHandler extends BaseTypeHandler<Date> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setString(i, Objects.toString(parameter.getTime()));
        } else {
            ps.setObject(i, parameter.getTime(), jdbcType.TYPE_CODE);
        }
    }

    @Override
    public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String i = rs.getString(columnName);
        if (rs.wasNull()) {
            return null;
        } else {
            return new Date(Long.valueOf(i));
        }
    }

    @Override
    public Date getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String i = rs.getString(columnIndex);
        if (rs.wasNull()) {
            return null;
        } else {
            return new Date(Long.valueOf(i));
        }
    }

    @Override
    public Date getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String i = cs.getString(columnIndex);
        if (cs.wasNull()) {
            return null;
        } else {
            return new Date(Long.valueOf(i));
        }
    }
}

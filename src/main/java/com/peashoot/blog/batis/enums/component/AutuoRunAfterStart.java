package com.peashoot.blog.batis.enums.component;

import com.peashoot.blog.batis.converter.MyEnumTypeHandler;
import com.peashoot.blog.batis.enums.base.BaseEnum;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

// @Component
public class AutuoRunAfterStart {
    // @PostConstruct
    @SuppressWarnings("unchecked")
    public static void injectAnnotationAttribute() throws NoSuchFieldException, IllegalAccessException {
        String packageName = "com.peashoot.blog.batis.enums";
        ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<>();
        resolverUtil.find(new ResolverUtil.IsA(BaseEnum.class), packageName);
        // 根据运行环境获取表名
        Class<?>[] baseEnumClasses = resolverUtil.getClasses().toArray(new Class<?>[0]);
        // 获取 Test 上的注解
        MappedTypes annoMappedType = MyEnumTypeHandler.class.getAnnotation(MappedTypes.class);

        if (annoMappedType == null) {
            throw new RuntimeException("please add @MappedType for MyEnumTypeHandler");
        }
        // 获取代理处理器
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(annoMappedType);
        // 过去私有 memberValues 属性
        Field f = invocationHandler.getClass().getDeclaredField("memberValues");
        f.setAccessible(true);
        // 获取实例的属性map
        Map<String, Object> memberValues = (Map<String, Object>)f.get(invocationHandler);
        // 修改属性值
        memberValues.put("value", baseEnumClasses);
    }
}

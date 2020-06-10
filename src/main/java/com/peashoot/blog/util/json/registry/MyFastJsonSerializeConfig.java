package com.peashoot.blog.util.json.registry;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.peashoot.blog.batis.enums.PermissionEnum;
import com.peashoot.blog.batis.enums.base.BaseEnum;
import com.peashoot.blog.util.json.deserializer.EnumArrayCodec;
import com.peashoot.blog.util.json.deserializer.EnumCodec;

public class MyFastJsonSerializeConfig extends SerializeConfig {
    public static final MyFastJsonSerializeConfig instance = new MyFastJsonSerializeConfig();
    public MyFastJsonSerializeConfig() {
        super();
        initSerialization();
    }

    private void initSerialization() {
        put(PermissionEnum[].class, EnumArrayCodec.instance);
        put(BaseEnum.class, EnumCodec.instance);
    }
}

package com.peashoot.blog.util.json.registry;

import com.alibaba.fastjson.parser.ParserConfig;
import com.peashoot.blog.batis.enums.PermissionEnum;
import com.peashoot.blog.batis.enums.base.BaseEnum;
import com.peashoot.blog.util.json.deserializer.EnumArrayCodec;
import com.peashoot.blog.util.json.deserializer.EnumCodec;

public class MyFastJsonParseConfig extends ParserConfig {
    public static final MyFastJsonParseConfig instance = new MyFastJsonParseConfig();
    public MyFastJsonParseConfig() {
        super();
        initDeserializers();
    }
    private void initDeserializers() {
        putDeserializer(PermissionEnum[].class, EnumArrayCodec.instance);
        putDeserializer(BaseEnum.class, EnumCodec.instance);
    }
}

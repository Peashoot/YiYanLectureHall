package com.peashoot.blog.batis.entity.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public abstract class StringPrimaryEntity extends BaseEntity<String>{
    public StringPrimaryEntity() {
        id = UUID.randomUUID().toString().replace("-", "");
    }
}

package com.peashoot.blog.entity.base;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class BaseEntity<TPrimary> implements Serializable {
    protected TPrimary id;
}

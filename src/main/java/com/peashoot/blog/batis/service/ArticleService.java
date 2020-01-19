package com.peashoot.blog.batis.service;

import com.peashoot.blog.batis.entity.Article;
import com.peashoot.blog.batis.service.base.InsertService;
import com.peashoot.blog.batis.service.base.RemoveService;
import com.peashoot.blog.batis.service.base.SelectService;
import com.peashoot.blog.batis.service.base.UpdateService;

public interface ArticleService extends InsertService<Article>, UpdateService<Article>,
        SelectService<Article, Integer>, RemoveService<Article, Integer> {
}

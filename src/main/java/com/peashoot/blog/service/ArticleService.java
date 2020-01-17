package com.peashoot.blog.service;

import com.peashoot.blog.entity.Article;
import com.peashoot.blog.service.base.InsertService;
import com.peashoot.blog.service.base.RemoveService;
import com.peashoot.blog.service.base.SelectService;
import com.peashoot.blog.service.base.UpdateService;

public interface ArticleService extends InsertService<Article>, UpdateService<Article>,
        SelectService<Article, Integer>, RemoveService<Article, Integer> {
}

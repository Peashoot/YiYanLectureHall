package com.peashoot.blog.batis.service;

import com.peashoot.blog.batis.entity.VisitorDO;
import com.peashoot.blog.batis.service.base.InsertService;
import com.peashoot.blog.batis.service.base.SelectService;

public interface VisitorService extends InsertService<VisitorDO>, SelectService<VisitorDO, Long> {
    /**
     * 根据访问IP和浏览器指纹获取访客信息
     * @param visitIP 访客访问IP
     * @param browser 浏览器指纹
     * @return 访客信息
     */
    VisitorDO selectVisitorByIPAndBrowser(String visitIP, String browser);

    /**
     * 新增访客并返回新增id
     * @param visitor 访客信息
     * @return 新增访客id
     */
    long insertWithReturnRecordID(VisitorDO visitor);
}

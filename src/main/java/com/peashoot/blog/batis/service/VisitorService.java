package com.peashoot.blog.batis.service;

import com.peashoot.blog.batis.entity.VisitorDO;
import com.peashoot.blog.batis.service.base.InsertService;
import com.peashoot.blog.batis.service.base.SelectService;
import com.peashoot.blog.batis.service.base.UpdateService;

public interface VisitorService extends InsertService<VisitorDO>, SelectService<VisitorDO, Long>, UpdateService<VisitorDO> {
    /**
     * 根据访问IP和浏览器指纹获取访客信息
     *
     * @param visitIP 访客访问IP
     * @param browser 浏览器指纹
     * @return 访客信息
     */
    VisitorDO selectByIpAndBrowser(String visitIP, String browser);

    /**
     * 新增访客并返回新增id
     *
     * @param visitor 访客信息
     * @return 新增访客id
     */
    long insertWithReturnRecordId(VisitorDO visitor);

    /**
     * 根据访客名称进行查询
     *
     * @param visitorName 访客名称
     * @return 访客信息
     */
    VisitorDO selectByVisitorName(String visitorName);
}

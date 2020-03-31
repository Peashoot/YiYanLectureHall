package com.peashoot.blog.batis.service;

import com.peashoot.blog.batis.entity.ExceptionRecordDO;
import com.peashoot.blog.batis.service.base.InsertService;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

public interface ExceptionRecordService{
    /**
     * 异步添加异常记录
     * @param exceptionRecordDO 异常记录
     * @return 异步处理结果
     */
    Future<Boolean> insertNewRecordAsync(ExceptionRecordDO exceptionRecordDO);
    /**
     * 根据查询时间段分页获取异常记录数据
     * @param pageSize 单页最大数据量
     * @param pageIndex 当前页码
     * @param searchStart 查询开始时间
     * @param searchEnd 查询结束时间
     * @return 符合记录数
     */
    List<ExceptionRecordDO> listPagedRecords(int pageSize, int pageIndex, Date searchStart, Date searchEnd);

    /**
     * 根据查询条件获取符合条件的记录总数
     * @param searchStart 查询开始时间
     * @param searchEnd 查询结束时间
     * @return 总记录数
     */
    Long countTotalRecords(Date searchStart, Date searchEnd);
}

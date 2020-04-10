package com.peashoot.blog.batis.mapper;

import com.peashoot.blog.batis.entity.ExceptionRecordDO;
import com.peashoot.blog.batis.mapper.base.InsertMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExceptionRecordMapper extends InsertMapper<ExceptionRecordDO> {
    /**
     * 根据查询时间段分页获取异常记录数据
     *
     * @param pageSize    单页最大数据量
     * @param pageIndex   当前页码
     * @param searchStart 查询开始时间
     * @param searchEnd   查询结束时间
     * @return 符合记录数
     */
    List<ExceptionRecordDO> listPagedRecords(@Param("pageSize") int pageSize, @Param("pageIndex") int pageIndex,
                                             @Param("searchStart") Date searchStart, @Param("searchEnd") Date searchEnd);

    /**
     * 根据查询条件获取符合条件的记录总数
     *
     * @param searchStart 查询开始时间
     * @param searchEnd   查询结束时间
     * @return 总记录数
     */
    Long countTotalRecords(@Param("searchStart") Date searchStart, @Param("searchEnd") Date searchEnd);

}

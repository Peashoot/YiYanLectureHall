package com.peashoot.blog.batis.service.impl;

import com.peashoot.blog.batis.entity.ExceptionRecordDO;
import com.peashoot.blog.batis.mapper.ExceptionRecordMapper;
import com.peashoot.blog.batis.service.ExceptionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

@Service
@EnableAsync
public class ExceptionRecordServiceImpl implements ExceptionRecordService {
    @Autowired
    private ExceptionRecordMapper exceptionRecordMapper;
    @Override
    public List<ExceptionRecordDO> listPagedRecords(int pageSize, int pageIndex, Date searchStart, Date searchEnd) {
        return null;
    }

    @Override
    public Long countTotalRecords(Date searchStart, Date searchEnd) {
        return null;
    }

    @Override
    @Async
    public Future<Boolean> insertNewRecordAsync(ExceptionRecordDO insertItem) {
        return new AsyncResult<>(exceptionRecordMapper.insert(insertItem) > 0);
    }
}

package com.peashoot.blog.batis.service.impl;

import com.peashoot.blog.batis.entity.CommentDO;
import com.peashoot.blog.batis.entity.VisitRecordDO;
import com.peashoot.blog.batis.mapper.CommentMapper;
import com.peashoot.blog.batis.mapper.SysUserMapper;
import com.peashoot.blog.batis.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service("commentService")
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public int insert(CommentDO insertItem) {
        return commentMapper.insert(insertItem);
    }

    @Override
    public int remove(Integer removeId) {
        return commentMapper.deleteByPrimaryKey(removeId);
    }

    @Override
    public int removeRange(List<Integer> removeIdList) {
        return commentMapper.deleteRangeByPrimaryKeys(removeIdList.toArray(new Integer[0]));
    }

    @Override
    public List<CommentDO> selectAll() {
        return commentMapper.selectAll();
    }

    @Override
    public CommentDO selectById(Integer id) {
        return commentMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<CommentDO> listPagedComments(int pageSize, int pageIndex, String articleId) {
        return commentMapper.listPagedComments(pageSize, pageIndex, articleId);
    }

    @Override
    public int countTotalRecords(String articleId) {
        return commentMapper.countTotalRecords(articleId);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public boolean updateSupportAndDisagreeState(int commentId, int agree, int disagree) {
        CommentDO visitRecordDO = commentMapper.selectByPrimaryKeyForUpdate(commentId);
        visitRecordDO.setSupportCount(visitRecordDO.getSupportCount() + agree);
        visitRecordDO.setAgainstCount(visitRecordDO.getAgainstCount() + disagree);
        return commentMapper.updateByPrimaryKey(visitRecordDO) > 0;
    }
}

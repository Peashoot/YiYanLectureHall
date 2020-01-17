package com.peashoot.blog.service.impl;

import com.peashoot.blog.entity.Comment;
import com.peashoot.blog.entity.SysUser;
import com.peashoot.blog.mapper.CommentMapper;
import com.peashoot.blog.mapper.SysUserMapper;
import com.peashoot.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("commentService")
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserMapper sysuserMapper;
    @Override
    public int insert(Comment insertItem) {
        if (insertItem.getUser() != null) {
            insertItem.setUserId(insertItem.getUser().getId());
        }
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
    public List<Comment> selectAll() {
        List<Comment> retList = commentMapper.selectAll();
        for (Comment comment : retList) {
            comment.setUser(sysuserMapper.selectByPrimaryKey(comment.getUserId()));
        }
        return retList;
    }

    @Override
    public Comment selectById(Integer id) {
        Comment comment = commentMapper.selectByPrimaryKey(id);
        comment.setUser(sysuserMapper.selectByPrimaryKey(comment.getUserId()));
        return comment;
    }
}

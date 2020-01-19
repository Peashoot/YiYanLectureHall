package com.peashoot.blog.batis.mapper;

import com.peashoot.blog.batis.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteRangeByPrimaryKeys(@Param("userIds") Integer[] userIds);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    SysUser selectByUsername(String username);

    SysUser selectByUsernameOrEmail(String usernameOrEmail);

    SysUser selectByEmail(String email);

    List<SysUser> selectAll();

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);
}
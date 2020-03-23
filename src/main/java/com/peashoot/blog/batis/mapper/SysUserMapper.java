package com.peashoot.blog.batis.mapper;

import com.peashoot.blog.batis.entity.SysUserDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteRangeByPrimaryKeys(@Param("userIds") Integer[] userIds);

    int insert(SysUserDO record);

    int insertSelective(SysUserDO record);

    SysUserDO selectByPrimaryKey(Integer id);

    SysUserDO selectByUsername(String username);

    SysUserDO selectByUsernameOrEmail(String usernameOrEmail);

    SysUserDO selectByEmail(String email);

    List<SysUserDO> selectAll();

    int updateByPrimaryKeySelective(SysUserDO record);

    int updateByPrimaryKey(SysUserDO record);

    int getIdByUsername(String username);
}
package com.peashoot.blog.batis.service;

import com.peashoot.blog.batis.entity.SysUser;

public interface AuthService {
    boolean register(SysUser userToAdd);
    String login(String username, String password);
    String refresh(String oldToken);
}

package com.peashoot.blog.service;

import com.peashoot.blog.entity.SysUser;

public interface AuthService {
    boolean register(SysUser userToAdd);
    String login(String username, String password);
    String refresh(String oldToken);
}

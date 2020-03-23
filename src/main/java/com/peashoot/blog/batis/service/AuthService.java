package com.peashoot.blog.batis.service;

import com.peashoot.blog.batis.entity.SysUserDO;
import com.peashoot.blog.exception.UserNameOccupiedException;

public interface AuthService {
    /**
     * 注册新用户
     *
     * @param userToAdd 新用户
     * @return 是否成功
     */
    boolean insertSysUser(SysUserDO userToAdd) throws UserNameOccupiedException;

    /**
     * 用户登录
     *
     * @param username           用户名
     * @param password           密码
     * @param loginIP            登录IP
     * @param browserFingerprint 浏览器指纹
     * @return 新生成的token
     */
    String login(String username, String password, String loginIP, String browserFingerprint);

    /**
     * 刷新用户token
     *
     * @param oldToken 旧的token
     * @return 新生成的token
     */
    String refresh(String oldToken);

    /**
     * 重置密码
     *
     * @param username 用户名
     * @param oldPwd   旧密码
     * @param newPwd   新密码
     * @return 是否成功
     */
    boolean changePassword(String username, String oldPwd, String newPwd);

    /**
     * 用户登出
     *
     * @param username 用户名
     * @return 是否成功
     */
    boolean logOut(String username);

    /**
     * 发送重置密码邮件
     *
     * @param sysUser 用户信息
     * @return 是否成功
     */
    boolean sendResetPasswordEmail(SysUserDO sysUser);

    /**
     * 重置密码
     *
     * @param username
     * @param applySerial
     * @param newPassword
     * @return
     */
    boolean resetPassword(String username, String applySerial, String newPassword);
}

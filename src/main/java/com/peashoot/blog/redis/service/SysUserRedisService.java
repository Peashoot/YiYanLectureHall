package com.peashoot.blog.redis.service;

import java.util.Date;

public interface SysUserRedisService {
    /**
     * 保存登录时的浏览器指针和访问IP
     * @param username 用户名
     * @param token 登录时生成的token
     * @param expireTime 过期时间
     */
    void recordGenerateToken(String username, String token, long expireTime);

    /**
     *  将用户请求的token和缓存的token做比较
     * @param username 用户名
     * @param token 用户请求的token
     * @return 是否已登录
     */
    boolean checkIfNeedReLogin(String username, String token);

    /**
     * 移除用户token信息
     * @param username 用户名
     * @return 是否成功
     */
    boolean removeUserTokenInfo(String username);

    /**
     * 保存重置密码的申请编号
     * @param username 用户名
     * @param applySerial 申请编号
     */
    void saveResetPwdApplySerial(String username, String applySerial);

    /**
     * 检查重置密码的申请编号
     * @param username 用户名
     * @param applySerial 申请编号
     * @return 是否匹配
     */
    boolean checkIfMatchResetPwdApplySerial(String username, String applySerial);
}

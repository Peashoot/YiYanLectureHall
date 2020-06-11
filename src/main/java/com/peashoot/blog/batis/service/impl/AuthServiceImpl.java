package com.peashoot.blog.batis.service.impl;

import com.peashoot.blog.jwt.JwtTokenUtil;
import com.peashoot.blog.batis.entity.SysUserDO;
import com.peashoot.blog.exception.UserNameOccupiedException;
import com.peashoot.blog.batis.service.AuthService;
import com.peashoot.blog.batis.service.SysUserService;
import com.peashoot.blog.redis.service.SysUserRedisService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("authService")
public class AuthServiceImpl implements AuthService {
    /**
     * 用户信息操作类
     */
    private final SysUserService sysUserService;
    /**
     * token生成
     */
    private final JwtTokenUtil jwtTokenUtil;
    /**
     * 身份认证
     */
    private final AuthenticationManager authenticationManager;
    /**
     * token头
     */
    @Value("${peashoot.blog.http.jwt.tokenHead}")
    private String tokenHead;
    /**
     * 登录过期时间
     */
    @Value("${peashoot.blog.http.jwt.expiration}")
    private Long loginExpiration;
    /**
     * 是否支持单点登录
     */
    @Value("${peashoot.blog.sso.enabled}")
    private boolean singleSignOn = false;
    /**
     * 申请过期时间
     */
    @Value("${peashoot.blog.http.apply.expiration}")
    private Long applyExpiration;
    /**
     * 申请幂等时间
     */
    @Value("${peashoot.blog.http.apply.idempotent.interval}")
    private Long applyIdempotentInterval;
    /**
     * 加密方式
     */
    private final PasswordEncoder passwordEncoder;
    /**
     * RedisToken操作类
     */
    private final SysUserRedisService sysUserRedisService;

    public AuthServiceImpl(SysUserService sysUserService, JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, SysUserRedisService sysUserRedisService) {
        this.sysUserService = sysUserService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.sysUserRedisService = sysUserRedisService;
    }

    @Override
    public boolean insertSysUser(@NotNull SysUserDO userToAdd) throws UserNameOccupiedException {
        final String username = userToAdd.getUsername();
        try {
            // 如果没有找到相关用户，继续注册操作；否则抛出用户已注册异常
            sysUserService.loadUserByUsername(username);
            throw new UserNameOccupiedException("Username or email has been registered.");
        } catch (UsernameNotFoundException ex) {
            // 对密码进行加密
            final String rawPassword = userToAdd.saltPatchWork();
            userToAdd.setPassword(passwordEncoder.encode(rawPassword));
            userToAdd.setLastPasswordResetTime(new Date());
            if (sysUserService.insert(userToAdd) > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String login(String username, String password, String loginIp, String browserFingerprint) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        // 检查密码
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Reload password post-security so we can generate token
        final UserDetails userDetails = sysUserService.loadUserByUsername(username);
        // 生成新的token
        return jwtTokenUtil.generateToken(userDetails, loginIp, browserFingerprint);
    }

    @Override
    public String refresh(@NotNull String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        SysUserDO user = (SysUserDO) sysUserService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetTime())) {
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }

    @Override
    public boolean changePassword(String username, String oldPwd, String newPwd) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, oldPwd);
        authenticationManager.authenticate(upToken);
        return updateUserPassword(username, newPwd);
    }

    @Override
    public boolean logOut(String username) {
        return sysUserRedisService.removeUserTokenInfo(username);
    }

    @Override
    public boolean sendResetPasswordEmail(SysUserDO sysUser) {
        try {
            String serialNumber = sysUserRedisService.saveResetPwdApplySerial(sysUser.getUsername(), applyExpiration, applyIdempotentInterval);
            // TODO：生成重置链接，发送邮件
            // 链接模板、邮件模板
        } catch (Exception ex) {
            return false;
        }
        return false;
    }

    @Override
    public boolean resetPassword(String username, String applySerial, String newPwd) {
        if (!sysUserRedisService.checkIfMatchResetPwdApplySerial(username, applySerial)) {
            return false;
        }
        return updateUserPassword(username, newPwd);
    }

    /**
     * 更新用户密码
     *
     * @param username 用户名
     * @param newPwd   密码
     * @return 是否成功
     */
    private boolean updateUserPassword(String username, String newPwd) {
        SysUserDO userToUpdate = (SysUserDO) sysUserService.loadUserByUsername(username);
        if (userToUpdate == null) {
            return false;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userToUpdate.setPassword(newPwd);
        final String rawPassword = userToUpdate.saltPatchWork();
        userToUpdate.setPassword(encoder.encode(rawPassword));
        Date current = new Date();
        userToUpdate.setLastPasswordResetTime(current);
        userToUpdate.setUpdateTime(current);
        return sysUserService.update(userToUpdate) > 0;
    }
}

package com.peashoot.blog.batis.service.impl;

import com.peashoot.blog.jwt.JwtTokenUtil;
import com.peashoot.blog.batis.entity.SysUser;
import com.peashoot.blog.exception.UserNameOccupiedException;
import com.peashoot.blog.batis.service.AuthService;
import com.peashoot.blog.batis.service.SysUserService;
import com.peashoot.blog.redis.service.LoginRedisService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private SysUserService sysUserService;
    /**
     * token生成
     */
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    /**
     * 身份认证
     */
    @Autowired
    private AuthenticationManager authenticationManager;
    /**
     * token头
     */
    @Value("${peashoot.blog.jwt.tokenHead}")
    private String tokenHead;
    /**
     * 登录过期时间
     */
    @Value("${peashoot.blog.jwt.expiration}")
    private Long expiration;
    /**
     * 是否支持单点登录
     */
    @Value("${peashoot.blog.sso.enabled}")
    private boolean singleSignOn = false;
    /**
     * 加密方式
     */
    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * redis登录信息服务
     */
    @Autowired
    private LoginRedisService loginRedisService;

    @Override
    public boolean register(@NotNull SysUser userToAdd) throws UserNameOccupiedException {
        final String username = userToAdd.getUsername();
        try {
            // 如果没有找到相关用户，继续注册操作；否则抛出用户已注册异常
            sysUserService.loadUserByUsername(username);
            throw new UserNameOccupiedException("Username or email has been registered.");
        } catch (UsernameNotFoundException ex) {
            // 对密码进行加密
            final String rawPassword = userToAdd.saltPatchWork();
            userToAdd.setPassword(passwordEncoder.encode(rawPassword));
            userToAdd.setLastPasswordResetDate(new Date());
            if (sysUserService.insert(userToAdd) > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String login(String username, String password, String loginIP, String browserFingerprint) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        // 检查密码
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Reload password post-security so we can generate token
        final UserDetails userDetails = sysUserService.loadUserByUsername(username);
        // 生成新的token
        final String token = jwtTokenUtil.generateToken(userDetails);
        return token;
    }

    @Override
    public String refresh(@NotNull String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        SysUser user = (SysUser) sysUserService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }

    @Override
    public boolean changePassword(String username, String oldPwd, String newPwd) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, oldPwd);
        authenticationManager.authenticate(upToken);
        SysUser userToUpdate = (SysUser) sysUserService.loadUserByUsername(username);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userToUpdate.setPassword(newPwd);
        final String rawPassword = userToUpdate.saltPatchWork();
        userToUpdate.setPassword(encoder.encode(rawPassword));
        Date current = new Date();
        userToUpdate.setLastPasswordResetDate(current);
        userToUpdate.setUpdateTime(current);
        return sysUserService.update(userToUpdate) > 0;
    }
}

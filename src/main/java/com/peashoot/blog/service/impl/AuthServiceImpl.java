package com.peashoot.blog.service.impl;

import com.peashoot.blog.common.JwtTokenUtil;
import com.peashoot.blog.entity.SysUser;
import com.peashoot.blog.exception.UserNameOccupiedException;
import com.peashoot.blog.service.AuthService;
import com.peashoot.blog.service.SysUserService;
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
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("authService")
public class AuthServiceImpl implements AuthService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    public boolean register(@NotNull SysUser userToAdd) {
        final String username = userToAdd.getUsername();
        try {
            // 如果没有找到相关用户，则返回
            sysUserService.loadUserByUsername(username);
            throw new UserNameOccupiedException("Username or email has been registered.");
        } catch (UsernameNotFoundException ex) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            final String rawPassword = userToAdd.saltPatchWork();
            userToAdd.setPassword(encoder.encode(rawPassword));
            userToAdd.setLastPasswordResetDate(new Date());
            if (sysUserService.insert(userToAdd) > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Reload password post-security so we can generate token
        final UserDetails userDetails = sysUserService.loadUserByUsername(username);
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
}

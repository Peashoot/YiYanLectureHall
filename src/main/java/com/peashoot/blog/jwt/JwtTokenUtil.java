package com.peashoot.blog.jwt;

import com.peashoot.blog.batis.entity.SysUserDO;
import com.peashoot.blog.redis.service.SysUserRedisService;
import com.peashoot.blog.util.Constant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil implements Serializable {
    /**
     * 用户名键名
     */
    private static final String CLAIM_KEY_USERNAME = "sub";
    /**
     * 创建时间键名
     */
    private static final String CLAIM_KEY_CREATED = "created";
    /**
     * 登录IP键名
     */
    private static final String CLAIM_KEY_LOGINIP = "address";
    /**
     * 浏览器指纹键名
     */
    private static final String CLAIM_KEY_BROWSERFINGERPRINT = "bfp";

    /**
     * token加密密钥
     */
    @Value("${peashoot.blog.http.jwt.secret}")
    private String secret;

    /**
     * token有效时间（秒）
     */
    @Value("${peashoot.blog.http.jwt.expiration}")
    private Long expiration = 3600L;
    /**
     * token中是否包含访问IP信息
     */
    @Value("${peashoot.blog.http.jwt.contains.visit_ip}")
    private Boolean tokenWithVisitIp = true;
    /**
     * token中是否包含浏览器指纹信息
     */
    @Value("${peashoot.blog.http.jwt.contains.browser_fingerprint}")
    private Boolean tokenWithBrowserFingerprint = true;
    /**
     * RedisToken操作类
     */
    private final SysUserRedisService sysUserRedisService;

    public JwtTokenUtil(SysUserRedisService sysUserRedisService) {
        this.sysUserRedisService = sysUserRedisService;
    }

    /**
     * 从token中获取用户名
     * @param token token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 从token中获取创建日期
     * @param token token
     * @return 创建日期
     */
    private Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    /**
     * 从token中获取过期日期
     * @param token token
     * @return 创建日期
     */
    private Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    /**
     * 从token中获取其他信息
     * @param token token
     * @return 其他信息
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 生成过期日期
     * @return 过期日期
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * Constant.MILLISECONDS_PEY_SECOND);
    }

    /**
     * 判断token是否过期
     * @param token token
     * @return 是否过期
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 判断token生成时间是否早于重置密码时间（用户重置密码后需要重新登录）
     * @param created token创建时间
     * @param lastPasswordReset 上一次密码重置时间
     * @return 是否需要重新生成token
     */
    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    /**
     * 生成token
     * @param userDetails 用户信息
     * @param loginIp 登录IP
     * @param browserFingerprint 浏览器指纹
     * @return token
     */
    public String generateToken(UserDetails userDetails, String loginIp, String browserFingerprint) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        if (tokenWithVisitIp) {
            claims.put(CLAIM_KEY_LOGINIP, loginIp);
        }
        if (tokenWithBrowserFingerprint) {
            claims.put(CLAIM_KEY_BROWSERFINGERPRINT, browserFingerprint);
        }
        String token =  generateToken(claims);
        sysUserRedisService.recordGenerateToken(userDetails.getUsername(), token, System.currentTimeMillis() + expiration * Constant.MILLISECONDS_PEY_SECOND);
        return token;
    }

    /**
     * 生成token
     * @param claims 附加内容
     * @return token
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 判断token是否可以刷新
     * @param token 原token
     * @param lastPasswordReset 上一次密码重置时间
     * @return 是否需要刷新
     */
    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && !isTokenExpired(token);
    }

    /**
     * 刷新token
     * @param token 原token
     * @return 新token
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 验证token是否有效
     * @param token token
     * @param userDetails 用户信息
     * @return 有效性
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        SysUserDO user = (SysUserDO) userDetails;
        final String username = getUsernameFromToken(token);
        final Date created = getCreatedDateFromToken(token);
        return (
                username.equals(user.getUsername())
                        && !isTokenExpired(token)
                        && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate()))
                        && !sysUserRedisService.checkIfNeedReLogin(username, token);
    }
}

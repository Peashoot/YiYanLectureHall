package com.peashoot.blog.exception;

/**
 * 用户不匹配异常
 */
public class UserUnmatchedException extends RuntimeException {
    public UserUnmatchedException() {
        super("unmatched user");
    }
}

package com.peashoot.blog.exception;

import org.springframework.security.core.AuthenticationException;

public class UserNameOccupiedException extends AuthenticationException {
    public UserNameOccupiedException(String msg) {
        super(msg);
    }
}

package com.peashoot.blog.exception;

import javax.security.sasl.AuthenticationException;

public class UserHasLoginException extends AuthenticationException {
    public UserHasLoginException(String msg) {
        super(msg);
    }
}

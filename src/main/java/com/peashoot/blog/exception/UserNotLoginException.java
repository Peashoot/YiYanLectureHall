package com.peashoot.blog.exception;

import javax.security.sasl.AuthenticationException;

public class UserNotLoginException extends AuthenticationException {
    public UserNotLoginException() {
        super("No logged in user found");
    }
}

package com.carrental.entity.exception;

import java.text.MessageFormat;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super(MessageFormat.format("Username {0} already exists.", username));
    }
}

package com.carrental.entity.exception;

import java.text.MessageFormat;

public class UserDoesNotExistsException extends RuntimeException {
    public UserDoesNotExistsException(String username) {
        super(MessageFormat.format("User {0] does not exist.", username));
    }
}

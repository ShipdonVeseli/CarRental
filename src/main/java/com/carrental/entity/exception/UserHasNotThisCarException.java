package com.carrental.entity.exception;

import java.text.MessageFormat;

public class UserHasNotThisCarException extends RuntimeException {
    public UserHasNotThisCarException(final Long userId) {
        super(MessageFormat.format("User {0} has not this car.", userId));
    }
}

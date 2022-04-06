package com.carrental.entity.exception;

import java.text.MessageFormat;

public class CarDoesNotExistsException extends RuntimeException {
    public CarDoesNotExistsException(Long carId) {
        super(MessageFormat.format("Car {0] does not exist.", carId));
    }
}

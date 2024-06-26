package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class RestrictException extends RuntimeException {
    private HttpStatus code;

    public RestrictException(String message) {
        super(message);
        this.code = HttpStatus.NOT_FOUND;
    }
}

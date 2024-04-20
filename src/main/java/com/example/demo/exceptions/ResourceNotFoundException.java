package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException {

    private HttpStatus code;

    public ResourceNotFoundException(String message, HttpStatus code) {
        super(message);
        this.code = code;
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException() {
        super();
    }
}

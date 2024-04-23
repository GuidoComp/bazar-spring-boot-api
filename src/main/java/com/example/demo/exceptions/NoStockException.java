package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class NoStockException extends RuntimeException {
    private HttpStatus code;

    public NoStockException(String message) {
        super(message);
        this.code = HttpStatus.NOT_FOUND;
    }
}

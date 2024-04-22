package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class EqualProductsIds extends RuntimeException {
    private HttpStatus code;

    public EqualProductsIds(String message) {
        super(message);
        this.code = HttpStatus.NOT_FOUND;
    }

    public EqualProductsIds() {
        super();
    }

    public EqualProductsIds(String message, HttpStatus code) {
        super(message);
        this.code = code;
    }
}

package com.example.demo.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseWithMessage<T> {
    private T data;
    private String message;

    public ResponseWithMessage(T data, String message) {
        this.data = data;
        this.message = message;
    }
}

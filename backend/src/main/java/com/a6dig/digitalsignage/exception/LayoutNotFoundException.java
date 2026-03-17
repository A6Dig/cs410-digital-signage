package com.a6dig.digitalsignage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LayoutNotFoundException extends RuntimeException{
    public LayoutNotFoundException(String message) {
        super(message);
    }
}

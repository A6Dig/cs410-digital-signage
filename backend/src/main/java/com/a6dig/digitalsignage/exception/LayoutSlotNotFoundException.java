package com.a6dig.digitalsignage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LayoutSlotNotFoundException extends RuntimeException{
    public LayoutSlotNotFoundException(String message) {
        super(message);
    }
}

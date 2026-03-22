package com.a6dig.digitalsignage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LayoutNotFoundException extends RuntimeException{
    private final List<Map<String, String>> errors;

    public LayoutNotFoundException(String message, List<Map<String, String>> errors){
        super(message);
        this.errors = errors;
    }

    public List<Map<String, String>> getErrors() {
        return errors;
    }
}

package com.a6dig.digitalsignage.exception;

import java.util.List;
import java.util.Map;

public class ModuleNotFoundException extends RuntimeException{

    private final List<Map<String, String>> errors;

    public ModuleNotFoundException(String message, List<Map<String, String>> errors){
        super(message);
        this.errors = errors;
    }

    public List<Map<String, String>> getErrors() {
        return errors;
    }
}

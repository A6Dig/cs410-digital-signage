package com.a6dig.digitalsignage.exception;

import java.util.List;
import java.util.Map;

public class InvalidDeviceException extends IllegalArgumentException {
    private final List<Map<String, String>> errors;

    public InvalidDeviceException(String message, List<Map<String, String>> errors) {
        super(message);
        this.errors = errors;
    }

    public List<Map<String, String>> getErrors() {
        return errors;
    }
}

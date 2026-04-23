package com.a6dig.digitalsignage.exception;

import java.util.List;
import java.util.Map;

public class PairingNotFoundException extends IllegalArgumentException {
    private final List<Map<String, String>> errors;

    public PairingNotFoundException(String message, List<Map<String, String>> errors) {
        super(message);
        this.errors = errors;
    }

    public List<Map<String, String>> getErrors() {
        return errors;
    }
}

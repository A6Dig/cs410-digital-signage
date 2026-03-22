package com.a6dig.digitalsignage.util;

import java.util.Map;
public final class ErrorMessage {

    private ErrorMessage() {}

    public static Map<String, String> createErrorMessage(String message) {
        return Map.of("error", message);
    }
}
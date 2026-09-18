package com.campusflow.util;

public final class InputValidator {
    private InputValidator() { }

    public static boolean isValidText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidText(String value, int minimumLength) {
        return isValidText(value) && value.trim().length() >= minimumLength;
    }
}

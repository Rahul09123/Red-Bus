package com.shubilet.security_service.common.util;

/**

    Domain: Validation

    Provides lightweight, null-safe utility helpers for common string validation and comparison
    tasks. This class centralizes frequently used operations such as blank checking, numeric
    verification, and safe equality comparison to reduce duplication across the codebase. As a
    final utility class, it cannot be instantiated and is intended to support input validation
    and defensive programming throughout the application.

    <p>

        Technologies:

        <ul>
            <li>Core Java (utility methods only)</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @version 1.0
*/
public final class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Checks if the given string is null or contains only whitespace.
     */
    public static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }

    /**
     * Null-safe string equality check.
     */
    public static boolean nullSafeEquals(String a, String b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }

    /**
     * Checks if a string consists only of digits (0-9).
     */
    public static boolean isNumeric(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        for (char c : value.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}

package com.shubilet.expedition_service.common.util;

/****

    Domain: Validation

    Provides lightweight, null-safe utility helpers for common string validation and comparison tasks.
    This class centralizes frequently used operations such as null or blank checks, numeric string
    verification, and safe equality comparison to reduce duplication across the codebase. Designed as
    a final utility class, it cannot be instantiated and is intended to support defensive programming
    and input validation consistently across controller, service, and utility layers.

    <p>

        Technologies:

        <ul>
            <li>Core Java</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @version 2.0
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

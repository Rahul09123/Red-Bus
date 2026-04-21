package com.shubilet.member_service.common.constants;

public final class ValidationPatterns {
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$";

    ValidationPatterns() {
        throw new UnsupportedOperationException("Constant class cannot be instantiated.");
    }
}

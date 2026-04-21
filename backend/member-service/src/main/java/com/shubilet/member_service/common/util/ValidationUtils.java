package com.shubilet.member_service.common.util;

import com.shubilet.member_service.common.constants.ValidationPatterns;
import com.shubilet.member_service.common.enums.Gender;

public final class ValidationUtils {
    ValidationUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isValidGender(String gender_value) {
        return Gender.isValidGender(gender_value);
    }

    public static boolean isValidEmail(String email){
        return email.matches(ValidationPatterns.EMAIL_REGEX);
    }
    public static boolean isValidPassword(String password) {
        return password.matches(ValidationPatterns.PASSWORD_REGEX);
    }

}

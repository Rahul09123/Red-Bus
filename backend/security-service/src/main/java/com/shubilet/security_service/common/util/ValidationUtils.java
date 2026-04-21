package com.shubilet.security_service.common.util;



import com.shubilet.security_service.common.constants.ValidationPatterns;
import com.shubilet.security_service.common.enums.UserType;

/****

    Domain: ErrorHandling

    Provides a centralized utility for constructing standardized authentication and session-related
    error responses across the application. This class encapsulates common failure scenarios such as
    invalid input, session lifecycle errors, and critical system failures, and maps them to appropriate
    HTTP status codes while producing typed DTO-based {@link ResponseEntity} payloads.

    The utility ensures that the current {@link CookieDTO} context is always attached to responses,
    allowing clients to remain synchronized with the server-side session state after events such as
    session invalidation, expiration, or logout.

    <p>

        Responsibilities:

        <ul>
            <li>Generate consistent error messages for null/blank fields and invalid formats</li>
            <li>Handle authentication and session lifecycle errors (not found, expired, invalid, already logged in)</li>
            <li>Attach {@link CookieDTO} to all response DTOs for client-side session alignment</li>
            <li>Translate domain-specific error scenarios into correct HTTP status codes</li>
        </ul>

    </p>

    <p>

        Technologies:

        <ul>
            <li>Spring Web</li>
            <li>Core Java</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @version 3.0
*/
public final class ValidationUtils {

    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static boolean matches(String value, String regex) {
        return value != null && value.matches(regex);
    }

    /**

        Operation: Validate

        Validates whether the provided session key conforms to the expected structural and
        integrity requirements. The method first applies a regular expression check to ensure
        correct formatting, then extracts the embedded validation digits from fixed positions
        and verifies their checksum rule, where the sum of digits at positions 1, 6, and 21
        must equal 15. This ensures both syntactic correctness and internal consistency of
        the session key.

        <p>

            Uses:

            <ul>
                <li>{@code ValidationPatterns.SESSION_KEY_REGEX} for structural format validation</li>
                <li>{@code matches} helper for regex evaluation</li>
            </ul>

        </p>

        @param sessionKey the session key string to validate

        @return {@code true} if the session key matches the required structure and passes checksum validation,
        otherwise {@code false}
    */
    public static boolean isValidSessionKey(String sessionKey) {
        // basic regex validation
        if (!matches(sessionKey, ValidationPatterns.SESSION_KEY_REGEX)) return false;

        // validation digits
        int d1 = sessionKey.charAt(0) - '0';
        int d5 = sessionKey.charAt(5) - '0';
        int d17 = sessionKey.charAt(20) - '0';

        return d1 + d5 + d17 == 15;
    }

    public static boolean isValidUserType(String userType) {
        UserType type = UserType.fromCode(userType);
        return type != null;
    }
}

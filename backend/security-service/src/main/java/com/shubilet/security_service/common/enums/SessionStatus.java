package com.shubilet.security_service.common.enums;

import org.springframework.http.HttpStatus;
// Mirliva says: ACTIVE today, EXPIRED tomorrow.
// Just like motivation.

public enum SessionStatus {

    VALID(true, HttpStatus.OK, "SESSION_VALID"),
    EXPIRED(false, HttpStatus.UNAUTHORIZED, "SESSION_EXPIRED"),
    NOT_FOUND(false, HttpStatus.UNAUTHORIZED, "SESSION_NOT_FOUND");

    // Is this session usable in the system?
    private final boolean valid;

    // Which HTTP status should the API respond with?
    private final HttpStatus httpStatus;

    // Message or error key for client / i18n / ErrorMessages
    private final String messageKey;

    SessionStatus(boolean valid, HttpStatus httpStatus, String messageKey) {
        this.valid = valid;
        this.httpStatus = httpStatus;
        this.messageKey = messageKey;
    }

    public boolean isValid() {
        return valid;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessageKey() {
        return messageKey;
    }

}

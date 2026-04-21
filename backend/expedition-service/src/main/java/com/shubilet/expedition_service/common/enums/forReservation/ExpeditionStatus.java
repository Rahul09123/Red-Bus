package com.shubilet.expedition_service.common.enums.forReservation;

import org.springframework.http.HttpStatus;

public enum ExpeditionStatus {
    NOT_FOUND(false, HttpStatus.NOT_FOUND, "EXPEDITION_NOT_FOUND"),
    NOT_VALID(false, HttpStatus.BAD_REQUEST, "EXPEDITION_NOT_VALID"),
    INVALID_TIME(false, HttpStatus.BAD_REQUEST, "EXPEDITION_INVALID_TIME"),
    ALREADY_BOOKED(false, HttpStatus.CONFLICT, "EXPEDITION_ALREADY_BOOKED"),
    SUCCESS(true, HttpStatus.OK, "EXPEDITION_SUCCESS");


    // Is this session usable in the system?
    private final boolean valid;

    // Which HTTP status should the API respond with?
    private final HttpStatus httpStatus;

    // Message or error key for client / i18n / ErrorMessages
    private final String messageKey;

    ExpeditionStatus(boolean valid, HttpStatus httpStatus, String messageKey) {
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

package com.shubilet.expedition_service.common.constants;

public final class ErrorMessages {

    private ErrorMessages() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    // General error messages
    public static final String NULL_OR_EMPTY = " cannot be null or empty.";
    public static final String INVALID_FORMAT = " has an invalid format.";
    public static final String NOT_FOUND = " not found.";
    public static final String INCORRECT = " is incorrect.";
    public static final String NOT_VERIFIED = " is not verified.";
    public static final String CRITICAL_ERROR = "A critical error has occurred.";
    public static final String ALREADY_EXISTS = " already exists.";

    // Session
    public static final String SESSION_NOT_FOUND = "Session not found.";

    //Specific error messages
    public static final String SAME_CITY_ERROR_MESSAGE = "Arrival city cannot be the same as departure city.";
    public static final String ALREADY_BOOKED = " is already booked.";
    public static final String DATE_IN_PAST_ERROR = "The provided date is in the past.";
    public static final String CARD_NOT_ACTIVE = "The provided card is not active.";
}

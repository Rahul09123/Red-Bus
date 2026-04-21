package com.shubilet.member_service.common.constants;

public final class ErrorMessages {
    // General error messages
    public static final String NULL_OR_EMPTY = " cannot be Null or Empty.";
    public static final String INVALID_FORMAT = " has an Invalid Format.";
    public static final String NOT_FOUND = " not Found.";
    public static final String INCORRECT = " is Incorrect.";
    public static final String NOT_VERIFIED = " is not Verified.";
    public static final String CRITICAL_ERROR = "A Critical Error has been Occurred.";
    public static final String ALREADY_EXISTS = " is Already Exists.";

    private ErrorMessages() {
        throw new UnsupportedOperationException("Utility class");
    }

}

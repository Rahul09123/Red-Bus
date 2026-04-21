package com.shubilet.payment_service.common.constants;

public final class ValidationPatterns {

    private ValidationPatterns() {}

    public static final String CARD_NUMBER_16_DIGITS = "\\d{16}";
    public static final String EXPIRATION_MM_YY = "(0[1-9]|1[0-2])/([0-9]{2})";
    public static final String CVC_3_DIGITS = "\\d{3}";
}

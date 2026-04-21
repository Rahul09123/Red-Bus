package com.shubilet.payment_service.common.constants;

public final class ErrorMessages {

    private ErrorMessages() {}

    public static final String CARD_NUMBER_INVALID =
            "Card number must be exactly 16 digits";

    public static final String CARD_EXPIRATION_INVALID =
            "Expiration date must be in MM/YY format";

    public static final String CARD_CVC_INVALID =
            "CVC must be exactly 3 digits";

    public static final String CARD_HOLDER_NAME_REQUIRED =
            "Card holder name must not be blank";

    public static final String CARD_SURNAME_BLANK =
            "Card holder surname must not be blank";

    public static final String CUSTOMER_ID_REQUIRED =
            "Customer id must not be blank";
}

package com.shubilet.payment_service.common.util;

import static com.shubilet.payment_service.common.constants.ValidationPatterns.CARD_NUMBER_16_DIGITS;
import static com.shubilet.payment_service.common.constants.ValidationPatterns.CVC_3_DIGITS;

public final class ValidationUtils {

    private ValidationUtils() {}

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isValidCardNumber(String cardNumber) {
        if (isBlank(cardNumber)) return false;
        String digitsOnly = cardNumber.replaceAll("\\D", "");
        return digitsOnly.matches(CARD_NUMBER_16_DIGITS);
    }

    public static boolean isValidExpiration(String month, String year) {
        if (isBlank(month) || isBlank(year)) return false;

        String m = month.trim();
        String y = year.trim();

        if (m.length() == 1) {
            m = "0" + m;
        }

        if (!m.matches("(0[1-9]|1[0-2])")) {
            return false;
        }

        return y.matches("\\d{2}");
    }

    public static boolean isValidCvc(String cvc) {
        if (isBlank(cvc)) return false;
        String digitsOnly = cvc.replaceAll("\\D", "");
        return digitsOnly.matches(CVC_3_DIGITS);
    }
}

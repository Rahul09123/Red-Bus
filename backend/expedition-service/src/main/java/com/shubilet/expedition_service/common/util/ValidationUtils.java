package com.shubilet.expedition_service.common.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;

import com.shubilet.expedition_service.common.constants.ValidationPatterns;

/****

    Domain: Validation

    Provides centralized, reusable validation utilities for enforcing common data integrity and business
    format rules across the application. This utility class offers static helper methods for validating
    numeric constraints (such as bounded decimal precision), date format correctness, and temporal rules
    like preventing past-date usage. It is designed as a non-instantiable helper class to promote
    consistency and reduce duplication of validation logic throughout service and controller layers.

    <p>

        Technologies:

        <ul>
            <li>Java Time API</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @version 1.0
*/
public final class ValidationUtils {
    
    private ValidationUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static boolean isValidBigDouble(double value) {
        if(value < 0) {
            return false;
        }

        String valueStr = Double.toString(value);
        String[] parts = valueStr.split("\\.");
        if(parts.length == 2) {
            String integerPart = parts[0];
            String fractionalPart = parts[1];
            if(integerPart.length() > 10 || fractionalPart.length() > 2) {
                return false;
            }
        } else if(parts.length == 1) {
            String integerPart = parts[0];
            if(integerPart.length() > 10) {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    public static boolean isValidDate(String date) {
        return date != null && date.matches(ValidationPatterns.DATE_PATTERN);
    }

    public static boolean isDateNotInPast(String date, Instant referenceInstant) {
        try {
            LocalDate inputDate = LocalDate.parse(date);
            LocalDate referenceDate = referenceInstant.atZone(ZoneId.systemDefault()).toLocalDate();
            return !inputDate.isBefore(referenceDate);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}

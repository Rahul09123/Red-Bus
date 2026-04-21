package com.shubilet.security_service.common.regex;

import com.shubilet.security_service.common.constants.AppConstants;

/**

    Domain: Validation

    Provides the construction logic for the regular expression used to validate the structural
    format of session keys generated and consumed within the authentication subsystem. This
    utility class dynamically incorporates the configured character alphabet and assembles a
    strict regex pattern that enforces numeric validation digits, grouped alphanumeric segments,
    and consistent dash-separated formatting. It is final and non-instantiable, serving solely
    as a static helper for regex generation.

    <p>

        Technologies:

        <ul>
            <li>Core Java string composition</li>
            <li>{@code AppConstants} for alphabet configuration</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @version 1.0
*/
public final class SessionKeyRegex {

    private SessionKeyRegex() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**

        Operation: Build

        Constructs the full regular expression used to validate the structure of a formatted
        session key. The resulting pattern enforces placement of numeric validation digits,
        alphanumeric character groups, and fixed dash-separated segment lengths. It dynamically
        incorporates the allowed character alphabet from application constants, ensuring the
        regex remains consistent with the system’s defined character set.

        <p>

            Uses:

            <ul>
                <li>{@code AppConstants.ALPHABET} to define the allowed alphanumeric character class</li>
                <li>{@code StringBuilder}-style concatenation through string composition</li>
            </ul>

        </p>

        @return a fully constructed regular expression string for validating session key format
    */
    public static String build() {
        String alphabet = AppConstants.ALPHABET; 

        String cc = "[" + alphabet + "]";

        return "^\\d" + cc + "{3}-" +
               "\\d" + cc + "{3}-" +
               cc + "{4}-" +
               cc + "{4}-" +
               "\\d" + cc + "{3}-" +
               cc + "{4}-" +
               cc + "{4}-" +
               cc + "{4}$";
    }
}

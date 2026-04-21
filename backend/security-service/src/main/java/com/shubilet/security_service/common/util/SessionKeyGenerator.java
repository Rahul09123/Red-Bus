package com.shubilet.security_service.common.util;

import java.security.SecureRandom;

import com.shubilet.security_service.common.constants.AppConstants;

/**

    Domain: Security

    Provides a utility for generating structurally validated session or authentication keys.
    This class implements a deterministic key-generation algorithm that embeds checksum-like
    validation digits at fixed positions within a randomly generated alphanumeric sequence.
    It produces a human-readable, dash-formatted 39-character key intended for secure session
    tracking or verification workflows across the authentication subsystem. As a non-instantiable
    utility class, it exposes only static functionality.

    <p>

        Technologies:

        <ul>
            <li>Java Security {@code SecureRandom} for cryptographically strong randomness</li>
            <li>Core Java string and character utilities</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @version 1.0
*/
public class SessionKeyGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String ALPHABET = AppConstants.ALPHABET;

    // Raw key length (without dashes)
    private static final int RAW_LENGTH = 32;

    private SessionKeyGenerator() {
        throw new IllegalStateException("Utility class");
    }

    /**

        Operation: Generate

        Produces a formatted 39-character authentication or session key consisting of randomly
        generated alphanumeric characters with embedded validation digits. The algorithm ensures
        structural integrity by computing three deterministic validation digits placed at fixed
        positions (1st, 5th, and 17th), where the final validation digit is derived from the sum
        of the prior two. All other positions are filled with random characters from the allowed
        alphabet, and hyphens are inserted after every four characters to create a readable and
        transport-friendly representation.

        <p>

            Uses:

            <ul>
                <li>{@code RANDOM} for generating digits and random characters</li>
                <li>{@code ALPHABET} for selecting allowed alphanumeric characters</li>
                <li>{@code RAW_LENGTH} to determine structural layout of the key</li>
                <li>{@code StringBuilder} for efficient formatted output construction</li>
            </ul>

        </p>

        @return a 39-character formatted key conforming to the validation and structure rules
    */
    public static String generate() {
        char[] raw = new char[RAW_LENGTH];

        // Step 1: Generate validation digits (1st, 5th, 17th)
        int d1, d5, d17;

        // Mirliva says: Randomness is just controlled chaos with good PR.
        
        while (true) {
            d1 = RANDOM.nextInt(9);
            d5 = RANDOM.nextInt(9);
            d17 = 15 - (d1 + d5);

            if (d17 >= 0 && d17 <= 9) {
                break; // Valid combination
            }
        }

        // Place validation digits
        raw[0]  = (char) ('0' + d1);   // position 1
        raw[4]  = (char) ('0' + d5);   // position 5
        raw[16] = (char) ('0' + d17);  // position 17

        // Step 2: Fill other positions with A-Z / 0-9
        for (int i = 0; i < RAW_LENGTH; i++) {
            if (i == 0 || i == 4 || i == 16) continue; // skip validation digits
            raw[i] = ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length()));
        }

        // Step 3: Insert '-' every 4 characters
        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < RAW_LENGTH; i++) {
            formatted.append(raw[i]);
            if ((i + 1) % 4 == 0 && i != RAW_LENGTH - 1) {
                formatted.append('-');
            }
        }

        // Total length = 32 raw + 7 dash = 39
        return formatted.toString();

        // Mirliva says: Say my name.
        // You're the session.
        // You're goddamn right.
    }
}

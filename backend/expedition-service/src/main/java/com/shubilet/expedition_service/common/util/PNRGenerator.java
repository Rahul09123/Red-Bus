package com.shubilet.expedition_service.common.util;

import com.shubilet.expedition_service.common.constants.AppConstants;

/****

    Domain: Utility

    Provides a simple utility for generating unique Passenger Name Record (PNR) codes used to
    identify tickets within the system. This class generates a fixed-length alphanumeric code
    by randomly selecting characters from a predefined alphabet constant. It is intended to be
    used during ticket creation workflows to produce human-readable, compact identifiers.

    <p>

        Technologies:

        <ul>
            <li>Core Java</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @version 1.0
*/
public class PNRGenerator {
    
    public static String generatePNR() {
        StringBuilder pnr = new StringBuilder();
        String characters = AppConstants.ALPHABET;
        int length = 6;
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            pnr.append(characters.charAt(index));
        }
        return pnr.toString();
    }
}
// Mirliva says: This PNR is unique.
// Just like everyone else.
package com.shubilet.security_service.common.constants;

import java.time.Duration;
import java.time.Instant;

/**

    Domain: Configuration

    Provides centralized application-wide constants used across the security service and
    supporting modules. This utility class defines static configuration values such as
    application metadata, default locale, pagination defaults, character alphabets, and
    session-related timing parameters. By consolidating these constants, the class promotes
    maintainability, consistency, and reduces magic values throughout the codebase. It is
    non-instantiable and intended solely as a static configuration holder.

    <p>

        Technologies:

        <ul>
            <li>Core Java constant management</li>
            <li>{@code java.time.Instant} and {@code java.time.Duration} for time-based defaults</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @version 1.0
*/
public final class AppConstants {
    private AppConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final String APP_NAME = "ShuBilet";
    public static final String DEFAULT_LOCALE = "tr-TR";
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    public static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static final Instant DEFAULT_SESSION_EXPIRATION_DURATION = Instant.now().plus(Duration.ofHours(24));

    public static final String FIXED_DELAY_STRING = "${app.sweeper.session.cleanup-interval-ms:300000}"; // 5 minutes
    public static final String INITIAL_DELAY_STRING = "${app.sweeper.session.initial-delay-ms:60000}";  // 1 minute
    
}
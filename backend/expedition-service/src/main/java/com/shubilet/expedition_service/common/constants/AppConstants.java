package com.shubilet.expedition_service.common.constants;

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
    
}
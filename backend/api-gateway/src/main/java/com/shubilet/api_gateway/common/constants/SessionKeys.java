package com.shubilet.api_gateway.common.constants;

/**

    Domain: Session

    Provides a centralized set of constant keys used for storing and retrieving authentication-
    related attributes within the HTTP session. By defining these identifiers in a dedicated
    utility class, the application ensures consistency, avoids hard-coded string duplication,
    and reduces the risk of session attribute mismatches across controllers and services.
    This class is final and non-instantiable, serving solely as a static container for these
    well-defined session key names.

    <p>

        Technologies:

        <ul>
            <li>Core Java constant definition</li>
        </ul>
        
    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @version 1.0
*/
public final class SessionKeys {
    private SessionKeys() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final String USER_ID = "userId";
    public static final String USER_TYPE = "userType";
    public static final String AUTH_CODE = "authCode";
}


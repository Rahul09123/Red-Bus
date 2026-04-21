package com.shubilet.security_service.common.enums;

/**

    Domain: Authentication

    Defines the supported user roles within the authentication and authorization system.
    Each enum constant represents a logical user category (admin, company, customer) along
    with a storable string code used for persistence and session handling. The enum also
    provides a safe conversion method for resolving user types from external string values,
    ensuring consistent interpretation of role identifiers throughout the application.

    <p>

        Technologies:

        <ul>
            <li>Core Java enum for role representation</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @version 1.0
*/
public enum UserType {

    ADMIN("ADMIN"),
    COMPANY("COMPANY"),
    CUSTOMER("CUSTOMER");

    private final String code;

    UserType(String code) {
        this.code = code;
    }

    /**
     * String code representation that can be stored in DB or session.
     */
    public String getCode() {
        return code;
    }

    /**
     * Safely convert from string code to enum.
     */
    public static UserType fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (UserType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null; // or throw IllegalArgumentException if you prefer
    }
}

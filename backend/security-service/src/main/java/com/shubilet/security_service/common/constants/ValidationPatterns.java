package com.shubilet.security_service.common.constants;

import com.shubilet.security_service.common.regex.SessionKeyRegex;


public final class ValidationPatterns {

        private ValidationPatterns() {
                throw new UnsupportedOperationException("Utility class");
        }

        public static final String SESSION_KEY_REGEX = 
                SessionKeyRegex.build();
}
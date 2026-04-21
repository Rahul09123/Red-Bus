package com.shubilet.member_service.common.util;

public final class StringUtils {

    StringUtils() {
        throw new UnsupportedOperationException("Util class cannot be instantiated.");
    }
    public static boolean isNullOrBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}

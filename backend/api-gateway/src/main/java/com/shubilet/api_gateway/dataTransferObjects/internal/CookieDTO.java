package com.shubilet.api_gateway.dataTransferObjects.internal;

import com.shubilet.api_gateway.common.constants.SessionKeys;

public class CookieDTO {
    private String userId;
    private String userType;
    private String authCode;

    public CookieDTO() {

    }

    public CookieDTO(String userId, String userType, String authCode) {
        this.userId = userId;
        this.userType = userType;
        this.authCode = authCode;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAuthCode() {
        return authCode;
    }
    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getAttribute(String key) {
        if(key.equals(SessionKeys.USER_ID)) {
            return getUserId();
        } else if(key.equals(SessionKeys.USER_TYPE)) {
            return getUserType();
        } else if(key.equals(SessionKeys.AUTH_CODE)) {
            return getAuthCode();
        } else {
            throw new IllegalArgumentException("Invalid session key: " + key);
        }
    }

    public void setAttribute(String key, String value) {
        if(value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        if(key.equals(SessionKeys.USER_ID)) {
            setUserId(value);
            return;
        } else if(key.equals(SessionKeys.USER_TYPE)) {
            setUserType(value);
            return;
        } else if(key.equals(SessionKeys.AUTH_CODE)) {
            setAuthCode(value);
            return;
        }

        throw new IllegalArgumentException("Invalid session key: " + key);
    }

    public void removeAttribute(String key) {
        if(key.equals(SessionKeys.USER_ID)) {
            setUserId(null);
            return;
        } else if(key.equals(SessionKeys.USER_TYPE)) {
            setUserType(null);
            return;
        } else if(key.equals(SessionKeys.AUTH_CODE)) {
            setAuthCode(null);
            return;
        }

        throw new IllegalArgumentException("Invalid session key: " + key);
    }


}

package com.shubilet.api_gateway.dataTransferObjects.internal.requests.auth;

import com.shubilet.api_gateway.dataTransferObjects.internal.CookieDTO;

public class SessionCreationDTO {

    private CookieDTO cookie;
    private int userId;
    private String userType;

    public SessionCreationDTO() {
    }

    public SessionCreationDTO(CookieDTO cookie, int userId, String userType) {
        this.cookie = cookie;
        this.userId = userId;
        this.userType = userType;
    }

    public CookieDTO getCookie() {
        return cookie;
    }

    public void setCookie(CookieDTO cookie) {
        this.cookie = cookie;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
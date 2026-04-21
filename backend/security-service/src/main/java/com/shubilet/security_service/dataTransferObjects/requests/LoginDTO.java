package com.shubilet.security_service.dataTransferObjects.requests;

import com.shubilet.security_service.dataTransferObjects.CookieDTO;

public class LoginDTO {
    
    private CookieDTO cookie;
    private int userId;
    private String userType;

    public LoginDTO() {
    }

    public LoginDTO(CookieDTO cookie, int userId, String userType) {
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
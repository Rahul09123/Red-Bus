package com.shubilet.api_gateway.dataTransferObjects.internal.responses.auth;


import com.shubilet.api_gateway.dataTransferObjects.internal.CookieDTO;

public class MemberCheckMessageDTO {
    private CookieDTO cookie;
    private String message;
    private int userId;

    public MemberCheckMessageDTO(CookieDTO cookie, String message, int userId) {
        this.cookie = cookie;
        this.message = message;
        this.userId = userId;
    }

    public CookieDTO getCookie() {
        return cookie;
    }

    public void setCookie(CookieDTO cookie) {
        this.cookie = cookie;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

package com.shubilet.api_gateway.managers;

import com.shubilet.api_gateway.dataTransferObjects.internal.CookieDTO;
import jakarta.servlet.http.HttpSession;

public class HttpSessionManager {

    public HttpSessionManager() {

    }

    public CookieDTO fromSessionToCookieDTO(HttpSession httpSession) {
        String userId = httpSession.getAttribute("userId") ==  null ? null : httpSession.getAttribute("userId").toString();
        String userType = httpSession.getAttribute("userType") ==  null ? null : httpSession.getAttribute("userType").toString();
        String authCode = httpSession.getAttribute("authCode") ==  null ? null : httpSession.getAttribute("authCode").toString();
        return new CookieDTO(userId, userType, authCode);
    }

    public void updateSessionCookie(HttpSession httpSession, CookieDTO cookieDTO) {
        if (cookieDTO == null) {
            httpSession.setAttribute("userId", null);
            httpSession.setAttribute("userType", null);
            httpSession.setAttribute("authCode", null);
            return;
        }
        httpSession.setAttribute("userId", cookieDTO.getUserId());
        httpSession.setAttribute("userType", cookieDTO.getUserType());
        httpSession.setAttribute("authCode", cookieDTO.getAuthCode());
    }
}


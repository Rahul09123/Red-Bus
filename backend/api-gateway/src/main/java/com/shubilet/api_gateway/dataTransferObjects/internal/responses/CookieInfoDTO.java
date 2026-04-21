package com.shubilet.api_gateway.dataTransferObjects.internal.responses;

import com.shubilet.api_gateway.dataTransferObjects.internal.CookieDTO;

/**

    Domain: Messaging

    Provides a simple data transfer object for carrying textual response messages throughout
    the authentication and session-management API. This DTO is used to standardize the format
    of success and error messages returned to clients, enabling consistent response structures
    across controllers and utility layers.

    <p>

        Technologies:

        <ul>
            <li>Core Java DTO pattern</li>
        </ul>

    </p>

    @autor Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @version 1.0
*/
public class CookieInfoDTO {
    private CookieDTO cookie;
    private String message;

    public CookieInfoDTO(CookieDTO cookie, String message) {
        this.cookie = cookie;
        this.message = message;
    }

    public CookieDTO getCookie() {
        return cookie;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}

package com.shubilet.security_service.dataTransferObjects.responses;

import com.shubilet.security_service.dataTransferObjects.CookieDTO;

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
public class MessageDTO {
    private CookieDTO cookie;
    private String message;

    public MessageDTO(CookieDTO cookie, String message) {
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

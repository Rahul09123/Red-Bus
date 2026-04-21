package com.shubilet.expedition_service.dataTransferObjects.responses.message;

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
    private String message;

    public MessageDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package com.shubilet.security_service.dataTransferObjects.requests;

import com.shubilet.security_service.common.enums.SessionStatus;

/**

    Domain: Session

    Represents a data transfer object conveying the result of a backend session validation
    operation. This DTO carries both the {@link SessionStatus} indicating the state of the
    session (valid, expired, not found, etc.) and an optional descriptive message that may
    provide additional context for logging or client-facing responses. It enables clean
    communication of session evaluation outcomes between session services and controllers.

    <p>

        Technologies:

        <ul>
            <li>Core Java DTO pattern</li>
            <li>{@code SessionStatus} enum for structured session state representation</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @version 1.0
*/
public class StatusDTO {
    private SessionStatus status;
    private String message = "";

    public StatusDTO(SessionStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public StatusDTO(SessionStatus status) {
        this.status = status;
    }

    public SessionStatus getStatus() {
        return status;
    }
    public void setStatus(SessionStatus status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}

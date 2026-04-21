package com.shubilet.member_service.dataTransferObjects.responses;

import java.util.Date;

public class MessageDTO {
    private Date timestamp;
    private String message;

    public MessageDTO() {
        this.message = "";
        this.timestamp = new Date();
    }
    public MessageDTO(String message) {
        this.timestamp = new Date();
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}

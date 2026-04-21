package com.shubilet.api_gateway.dataTransferObjects;

import java.util.Date;

public class MessageDTO {
    private Date timestamp;
    private String message;

    public MessageDTO() {

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

package com.shubilet.payment_service.dataTransferObjects.responses;

public class MessageDTO {
    private String message; // detay

    public MessageDTO() {
    }

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

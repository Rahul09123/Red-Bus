package com.shubilet.payment_service.dataTransferObjects.responses;

import java.io.Serializable;

public class TicketPaymentResponseDTO implements Serializable {

    private String status; // SUCCESS / FAILED
    private String message;
    private int paymentId;
    private String ticketId; // Bilet servisine referans

    public TicketPaymentResponseDTO() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
}
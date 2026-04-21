package com.shubilet.api_gateway.dataTransferObjects.internal.responses.ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketsInternalDTO {
    private String message;
    private List<TicketInternalDTO> tickets;

    public TicketsInternalDTO(String message) {
        this.message = message;
        this.tickets = new ArrayList<>();
    }

    public TicketsInternalDTO(String message, List<TicketInternalDTO> tickets) {
        this.message = message;
        this.tickets = tickets;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public List<TicketInternalDTO> getTickets() {
        return tickets;
    }
    public void setTickets(List<TicketInternalDTO> tickets) {
        this.tickets = tickets;
    }
    
}

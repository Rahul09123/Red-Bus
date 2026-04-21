package com.shubilet.expedition_service.dataTransferObjects.responses.complex;

import java.util.ArrayList;
import java.util.List;

import com.shubilet.expedition_service.dataTransferObjects.responses.base.TicketDTO;

public class TicketsDTO {
    private String message;
    private List<TicketDTO> tickets;

    public TicketsDTO(String message) {
        this.message = message;
        this.tickets = new ArrayList<>();
    }

    public TicketsDTO(String message, List<TicketDTO> tickets) {
        this.message = message;
        this.tickets = tickets;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public List<TicketDTO> getTickets() {
        return tickets;
    }
    public void setTickets(List<TicketDTO> tickets) {
        this.tickets = tickets;
    }
    
}

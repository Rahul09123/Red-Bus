package com.shubilet.api_gateway.dataTransferObjects.external.responses.ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketsExternalDTO {
    private String message;
    private List<TicketExternalDTO> ticketsDTO;

    public TicketsExternalDTO(String message) {
        this.message = message;
        this.ticketsDTO = new ArrayList<>();
    }

    public TicketsExternalDTO(String message, List<TicketExternalDTO> ticketsDTO) {
        this.message = message;
        this.ticketsDTO = ticketsDTO;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public List<TicketExternalDTO> getTicketsDTO() {
        return ticketsDTO;
    }
    public void setTicketsDTO(List<TicketExternalDTO> ticketsDTO) {
        this.ticketsDTO = ticketsDTO;
    }
    
}

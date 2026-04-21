package com.shubilet.expedition_service.dataTransferObjects.responses.middle;

import com.shubilet.expedition_service.dataTransferObjects.responses.base.TicketDTO;

public class TicketInfoDTO {
    private String message;
    private TicketDTO ticketDTO;

    public TicketInfoDTO() {

    }

    public TicketInfoDTO(String message) {
        this.message = message;
        this.ticketDTO = new TicketDTO();
    }

    public TicketInfoDTO( TicketDTO ticketDTO, String message) {
        this.message = message;
        this.ticketDTO = ticketDTO;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public TicketDTO getTicketDTO() {
        return ticketDTO;
    }
    public void setTicketDTO(TicketDTO ticketDTO) {
        this.ticketDTO = ticketDTO;
    }
}

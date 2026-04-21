package com.shubilet.api_gateway.dataTransferObjects.internal.responses.ticket;

public class TicketInfoDTO {
    private String message;
    private TicketInternalDTO ticketInternalDTO;

    public TicketInfoDTO() {

    }

    public TicketInfoDTO(String message) {
        this.message = message;
        this.ticketInternalDTO = new TicketInternalDTO();
    }

    public TicketInfoDTO(TicketInternalDTO ticketInternalDTO, String message) {
        this.message = message;
        this.ticketInternalDTO = ticketInternalDTO;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public TicketInternalDTO getTicketDTO() {
        return ticketInternalDTO;
    }
    public void setTicketDTO(TicketInternalDTO ticketInternalDTO) {
        this.ticketInternalDTO = ticketInternalDTO;
    }
}

package com.shubilet.api_gateway.dataTransferObjects.external.responses.expeditionOperations;

import java.util.ArrayList;
import java.util.List;

public class SeatsForCompanyExternalDTO {
    private String message;
    private List<SeatForCompanyExternalDTO> seats;

    public SeatsForCompanyExternalDTO() {

    }

    public SeatsForCompanyExternalDTO(String message) {
        this.message = message;
        seats = new ArrayList<>();
    }

    public SeatsForCompanyExternalDTO(String message, List<SeatForCompanyExternalDTO> seats) {
        this.message = message;
        this.seats = seats;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SeatForCompanyExternalDTO> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatForCompanyExternalDTO> seats) {
        this.seats = seats;
    }
}

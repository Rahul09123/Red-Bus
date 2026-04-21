package com.shubilet.api_gateway.dataTransferObjects.internal.responses.expeditionOperations;

import java.util.ArrayList;
import java.util.List;

public class SeatsForCompanyInternalDTO {
    private String message;
    private List<SeatForCompanyInternalDTO> seats;

    public SeatsForCompanyInternalDTO() {

    }

    public SeatsForCompanyInternalDTO(String message) {
        this.message = message;
        seats = new ArrayList<>();
    }

    public SeatsForCompanyInternalDTO(String message, List<SeatForCompanyInternalDTO> seats) {
        this.message = message;
        this.seats = seats;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SeatForCompanyInternalDTO> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatForCompanyInternalDTO> seats) {
        this.seats = seats;
    }
}

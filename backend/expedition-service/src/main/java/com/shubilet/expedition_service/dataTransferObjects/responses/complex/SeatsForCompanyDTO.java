package com.shubilet.expedition_service.dataTransferObjects.responses.complex;

import java.util.ArrayList;
import java.util.List;

import com.shubilet.expedition_service.dataTransferObjects.responses.base.SeatForCompanyDTO;

public class SeatsForCompanyDTO {
    private String message;
    private List<SeatForCompanyDTO> seats;

    public SeatsForCompanyDTO() {

    }

    public SeatsForCompanyDTO(String message) {
        this.message = message;
        seats = new ArrayList<>();
    }

    public SeatsForCompanyDTO(String message, List<SeatForCompanyDTO> seats) {
        this.message = message;
        this.seats = seats;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public List<SeatForCompanyDTO> getSeats() {
        return seats;
    }
    public void setSeats(List<SeatForCompanyDTO> seats) {
        this.seats = seats;
    }
}

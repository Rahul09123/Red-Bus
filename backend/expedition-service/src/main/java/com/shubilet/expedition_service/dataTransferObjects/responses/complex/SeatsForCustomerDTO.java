package com.shubilet.expedition_service.dataTransferObjects.responses.complex;

import java.util.ArrayList;
import java.util.List;

import com.shubilet.expedition_service.dataTransferObjects.responses.base.SeatForCustomerDTO;

public class SeatsForCustomerDTO {
    private String message;
    private List<SeatForCustomerDTO> seats;

    public SeatsForCustomerDTO() {

    }

    public SeatsForCustomerDTO(String message) {
        this.message = message;
        seats = new ArrayList<>();
    }

    public SeatsForCustomerDTO(String message, List<SeatForCustomerDTO> seats) {
        this.message = message;
        this.seats = seats;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public List<SeatForCustomerDTO> getSeats() {
        return seats;
    }
    public void setSeats(List<SeatForCustomerDTO> seats) {
        this.seats = seats;
    }
}

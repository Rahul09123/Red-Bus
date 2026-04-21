package com.shubilet.expedition_service.dataTransferObjects.responses.forRepositories;

import java.math.BigDecimal;
import java.time.Instant;

public class ExpeditionForCompanyRepoDTO {
    private Integer expeditionId;
    private String departureCity;
    private String arrivalCity;
    private Instant dateAndTime;
    private BigDecimal price;
    private Integer duration;
    private Integer capacity;
    private Integer numberOfBookedSeats;
    private BigDecimal profit;


    public ExpeditionForCompanyRepoDTO(
        Integer expeditionId,
        String departureCity,
        String arrivalCity,
        Instant dateAndTime,
        BigDecimal price,
        Integer duration,
        Integer capacity,
        Integer numberOfBookedSeats,
        BigDecimal profit
    ) {
        this.expeditionId = expeditionId;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.dateAndTime = dateAndTime;
        this.price = price;
        this.duration = duration;
        this.capacity = capacity;
        this.numberOfBookedSeats = numberOfBookedSeats;
        this.profit = profit;
    }

    public Integer getExpeditionId() {
        return expeditionId;
    }
    public void setExpeditionId(Integer expeditionId) {
        this.expeditionId = expeditionId;
    }

    public String getDepartureCity() {
        return departureCity;
    }
    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getArrivalCity() {
        return arrivalCity;
    }
    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public Instant getDateAndTime() {
        return dateAndTime;
    }
    public void setDateAndTime(Instant dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getCapacity() {
        return capacity;
    }
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getNumberOfBookedSeats() {
        return numberOfBookedSeats;
    }
    public void setNumberOfBookedSeats(Integer numberOfBookedSeats) {
        this.numberOfBookedSeats = numberOfBookedSeats;
    }

    public BigDecimal getProfit() {
        return profit;
    }
    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }
}

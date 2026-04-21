package com.shubilet.expedition_service.dataTransferObjects.responses.forRepositories;

import java.math.BigDecimal;
import java.time.Instant;

public class ExpeditionForCustomerRepoDTO {
    private Integer expeditionId;
    private String departureCity;
    private String arrivalCity;
    private Instant dateAndTime;
    private BigDecimal price;
    private Integer duration;
    private Integer companyId;

    public ExpeditionForCustomerRepoDTO(
        Integer expeditionId,
        String departureCity,
        String arrivalCity,
        Instant dateAndTime,
        BigDecimal price,
        Integer duration,
        Integer companyId
    ) {
        this.expeditionId = expeditionId;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.dateAndTime = dateAndTime;
        this.price = price;
        this.duration = duration;
        this.companyId = companyId;
    }

    // Getters and Setters
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

    public Integer getCompanyId() {
        return companyId;
    }
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}


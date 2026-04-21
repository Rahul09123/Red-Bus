package com.shubilet.expedition_service.dataTransferObjects.responses.forRepositories;

import java.time.Instant;

public class TicketRepoDTO {
    private String PNR;
    private Integer seatNo;
    private Integer expeditionId;
    private Integer companyId;
    private String departureCity;
    private String arrivalCity;
    private Instant dateAndTime;
    private Integer duration;

    public TicketRepoDTO(
        String PNR,
        Integer seatNo,
        Integer expeditionId,
        Integer companyId,
        String departureCity,
        String arrivalCity,
        Instant dateAndTime,
        int duration
    ) {
        this.PNR = PNR;
        this.seatNo = seatNo;
        this.expeditionId = expeditionId;
        this.companyId = companyId;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.dateAndTime = dateAndTime;
        this.duration = duration;
    }

    public String getPNR() {
        return PNR;
    }
    public void setPNR(String PNR) {
        this.PNR = PNR;
    }

    public Integer getSeatNo() {
        return seatNo;
    }
    public void setSeatNo(Integer seatNo) {
        this.seatNo = seatNo;
    }

    public Integer getExpeditionId() {
        return expeditionId;
    }
    public void setExpeditionId(Integer expeditionId) {
        this.expeditionId = expeditionId;
    }

    public Integer getCompanyId() {
        return companyId;
    }
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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

    public Integer getDuration() {
        return duration;
    }
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
}

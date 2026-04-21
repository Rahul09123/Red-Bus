package com.shubilet.api_gateway.dataTransferObjects.internal.responses.ticket;

public class TicketInternalDTO {
    private String PNR;
    private int seatNo;
    private int expeditionId;
    private int companyId;
    private String departureCity;
    private String arrivalCity;
    private String date;
    private String time;
    private int duration;

    public TicketInternalDTO() {
    
    }

    public TicketInternalDTO(
        String PNR, 
        int seatNo, 
        int expeditionId, 
        int companyId, 
        String departureCity, 
        String arrivalCity, 
        String date, 
        String time, 
        int duration
    ) {
        this.PNR = PNR;
        this.seatNo = seatNo;
        this.expeditionId = expeditionId;
        this.companyId = companyId;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.date = date;
        this.time = time;
        this.duration = duration;
    }

    public String getPNR() {
        return PNR;
    }
    public void setPNR(String PNR) {
        this.PNR = PNR;
    }

    public int getSeatNo() {
        return seatNo;
    }
    public void setSeatNo(int seatNo) {
        this.seatNo = seatNo;
    }

    public int getExpeditionId() {
        return expeditionId;
    }
    public void setExpeditionId(int expeditionId) {
        this.expeditionId = expeditionId;
    }

    public int getCompanyId() {
        return companyId;
    }
    public void setCompanyId(int companyId) {
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

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
}

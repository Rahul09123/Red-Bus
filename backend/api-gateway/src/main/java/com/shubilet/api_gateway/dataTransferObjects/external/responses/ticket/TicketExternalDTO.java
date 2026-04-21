package com.shubilet.api_gateway.dataTransferObjects.external.responses.ticket;

public class TicketExternalDTO {
    private String PNR;
    private int seatNo;
    private int expeditionId;
    private String companyName;
    private String departureCity;
    private String arrivalCity;
    private String date;
    private String time;
    private int duration;

    public TicketExternalDTO() {

    }

    public TicketExternalDTO(
            String PNR,
            int seatNo,
            int expeditionId,
            String companyName,
            String departureCity,
            String arrivalCity,
            String date,
            String time,
            int duration
    ) {
        this.PNR = PNR;
        this.seatNo = seatNo;
        this.expeditionId = expeditionId;
        this.companyName = companyName;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

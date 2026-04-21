package com.shubilet.api_gateway.dataTransferObjects.external.responses.expeditionOperations;

public class ExpeditionSearchResultCustomerDTO {
    private int expeditionId;
    private String departureCity;
    private String arrivalCity;
    private String date;
    private String time;
    private double price;
    private int duration;
    private String companyName;
    
    public ExpeditionSearchResultCustomerDTO() {

    }

    public ExpeditionSearchResultCustomerDTO(
            int expeditionId,
            String departureCity,
            String arrivalCity,
            String date,
            String time,
            double price,
            int duration,
            String companyName
    ) {
        this.expeditionId = expeditionId;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.date = date;
        this.time = time;
        this.price = price;
        this.duration = duration;
        this.companyName = companyName;
    }


    public int getExpeditionId() {
        return expeditionId;
    }

    public void setExpeditionId(int expeditionId) {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}

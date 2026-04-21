package com.shubilet.api_gateway.dataTransferObjects.internal.requests.expeditionOperations;

public class

ExpeditionCreationInternalDTO {
    private int companyId;
    private String departureCity;
    private String arrivalCity;
    private String date;
    private String time;
    private double price;
    private int duration;
    private int capacity;

    public ExpeditionCreationInternalDTO() {

    }

    public ExpeditionCreationInternalDTO(
            String departureCity,
            String arrivalCity,
            String date,
            String time,
            double price,
            int duration,
            int companyId,
            int capacity
    ) {
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.date = date;
        this.time = time;
        this.price = price;
        this.duration = duration;
        this.companyId = companyId;
        this.capacity = capacity;
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

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

}

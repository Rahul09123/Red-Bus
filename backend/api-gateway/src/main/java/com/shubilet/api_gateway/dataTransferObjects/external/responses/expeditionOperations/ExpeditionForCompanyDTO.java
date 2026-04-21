package com.shubilet.api_gateway.dataTransferObjects.external.responses.expeditionOperations;

public class ExpeditionForCompanyDTO {
    private int expeditionId;
    private String departureCity;
    private String arrivalCity;
    private String date;
    private String time;
    private double price;
    private int duration;
    private int capacity;
    private int numberOfBookedSeats;
    private double profit;

    public ExpeditionForCompanyDTO() {

    }

    public ExpeditionForCompanyDTO(
            int expeditionId,
            String departureCity,
            String arrivalCity,
            String date,
            String time,
            double price,
            int duration,
            int capacity,
            int numberOfBookedSeats,
            double profit
    ) {
        this.expeditionId = expeditionId;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.date = date;
        this.time = time;
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

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }
}

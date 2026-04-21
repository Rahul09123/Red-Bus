package com.shubilet.expedition_service.dataTransferObjects.requests;

public class ViewDetailsForCustomerDTO {
    private String departureCity;
    private String arrivalCity;
    private String date;

    public ViewDetailsForCustomerDTO() {

    }

    public ViewDetailsForCustomerDTO(String departureCity, String arrivalCity, String date) {
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.date = date;
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
}

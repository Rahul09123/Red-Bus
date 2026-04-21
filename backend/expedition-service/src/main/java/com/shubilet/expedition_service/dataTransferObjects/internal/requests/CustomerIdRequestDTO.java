package com.shubilet.expedition_service.dataTransferObjects.internal.requests;

public class CustomerIdRequestDTO {

    private Integer customerId;

    public CustomerIdRequestDTO() {
    }

    public CustomerIdRequestDTO(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
}
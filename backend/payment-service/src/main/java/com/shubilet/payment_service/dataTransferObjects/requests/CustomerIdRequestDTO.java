package com.shubilet.payment_service.dataTransferObjects.requests;

import java.io.Serializable;

public class CustomerIdRequestDTO implements Serializable {

    private Integer customerId;

    public CustomerIdRequestDTO() {}

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
package com.shubilet.api_gateway.dataTransferObjects.internal.requests;

public class CustomerIdDTO {
    private int customerId;

    public CustomerIdDTO() {
    
    }

    public CustomerIdDTO(int customerId) {
        this.customerId = customerId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}

package com.shubilet.api_gateway.dataTransferObjects.internal.responses.expeditionOperations;

import java.util.Date;
import java.util.HashMap;

public class CustomerIdNameMapDTO {
    private HashMap<Integer, String> customers;
    private String message;
    private Date timestamp;

    public CustomerIdNameMapDTO(HashMap<Integer, String> customers, String message) {
        this.customers = customers;
        this.message = message;
        this.timestamp = new Date();
    }

    public CustomerIdNameMapDTO(String message) {
        this.message = message;
        this.timestamp = new Date();
    }

    public HashMap<Integer, String> getCustomers() {
        return customers;
    }

    public void setCustomers(HashMap<Integer, String> customers) {
        this.customers = customers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

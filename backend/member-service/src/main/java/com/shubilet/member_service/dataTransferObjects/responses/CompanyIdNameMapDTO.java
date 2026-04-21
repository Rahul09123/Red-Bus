package com.shubilet.member_service.dataTransferObjects.responses;

import java.util.Date;
import java.util.HashMap;

public class CompanyIdNameMapDTO {
    private HashMap<Integer, String> companies;
    private String message;
    private Date timestamp;

    public CompanyIdNameMapDTO(HashMap<Integer, String> companies, String message) {
        this.companies = companies;
        this.message = message;
        this.timestamp = new Date();
    }

    public CompanyIdNameMapDTO(String message) {
        this.message = message;
        this.timestamp = new Date();
    }

    public HashMap<Integer, String> getCompanies() {
        return companies;
    }

    public void setCompanies(HashMap<Integer, String> companies) {
        this.companies = companies;
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

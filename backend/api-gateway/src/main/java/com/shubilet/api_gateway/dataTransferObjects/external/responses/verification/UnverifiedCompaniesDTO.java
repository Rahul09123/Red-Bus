package com.shubilet.api_gateway.dataTransferObjects.external.responses.verification;

import java.util.LinkedList;
import java.util.List;

public class UnverifiedCompaniesDTO {
    private String message;
    private List<UnverifiedCompanyDTO> companies;

    public UnverifiedCompaniesDTO() {

    }

    public UnverifiedCompaniesDTO(String message) {
        this.message = message;
        this.companies = new LinkedList<>() {
        };
    }

    public UnverifiedCompaniesDTO(String message, List<UnverifiedCompanyDTO> companies) {
        this.message = message;
        this.companies = companies;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UnverifiedCompanyDTO> getCompanies() {
        return companies;
    }

    public void setCompanies(List<UnverifiedCompanyDTO> companies) {
        this.companies = companies;
    }
}

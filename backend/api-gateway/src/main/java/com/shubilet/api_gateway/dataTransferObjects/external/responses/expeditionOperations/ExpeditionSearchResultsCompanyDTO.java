package com.shubilet.api_gateway.dataTransferObjects.external.responses.expeditionOperations;

import java.util.ArrayList;
import java.util.List;

public class ExpeditionSearchResultsCompanyDTO {
    private String message;
    private List<ExpeditionSearchResultCustomerDTO> expeditions;

    public ExpeditionSearchResultsCompanyDTO() {

    }

    public ExpeditionSearchResultsCompanyDTO(String message) {
        this.message = message;
        expeditions = new ArrayList<>();
    }

    public ExpeditionSearchResultsCompanyDTO(String message, List<ExpeditionSearchResultCustomerDTO> expeditions) {
        this.message = message;
        this.expeditions = expeditions;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ExpeditionSearchResultCustomerDTO> getExpeditions() {
        return expeditions;
    }

    public void setExpeditions(List<ExpeditionSearchResultCustomerDTO> expeditions) {
        this.expeditions = expeditions;
    }
}

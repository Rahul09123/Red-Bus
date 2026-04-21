package com.shubilet.api_gateway.dataTransferObjects.internal.responses.expeditionOperations;

import java.util.ArrayList;
import java.util.List;

public class ExpeditionsForCustomerDTO {
    private String message;
    private List<ExpeditionForCustomerDTO> expeditions;

    public ExpeditionsForCustomerDTO() {

    }

    public ExpeditionsForCustomerDTO(String message) {
        this.message = message;
        expeditions = new ArrayList<>();
    }

    public ExpeditionsForCustomerDTO(String message, List<ExpeditionForCustomerDTO> expeditions) {
        this.message = message;
        this.expeditions = expeditions;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public List<ExpeditionForCustomerDTO> getExpeditions() {
        return expeditions;
    }
    public void setExpeditions(List<ExpeditionForCustomerDTO> expeditions) {
        this.expeditions = expeditions;
    }
}

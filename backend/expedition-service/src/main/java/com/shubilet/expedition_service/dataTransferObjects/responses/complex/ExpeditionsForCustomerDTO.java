package com.shubilet.expedition_service.dataTransferObjects.responses.complex;

import java.util.ArrayList;
import java.util.List;

import com.shubilet.expedition_service.dataTransferObjects.responses.base.ExpeditionForCustomerDTO;

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

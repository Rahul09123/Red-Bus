package com.shubilet.expedition_service.dataTransferObjects.responses.complex;

import java.util.ArrayList;
import java.util.List;

import com.shubilet.expedition_service.dataTransferObjects.responses.base.ExpeditionForCompanyDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.message.MessageDTO;

public class ExpeditionsForCompanyDTO {
    private String message;
    private List<ExpeditionForCompanyDTO> expeditions;

    public ExpeditionsForCompanyDTO() {

    }

    public ExpeditionsForCompanyDTO(String message) {
        this.message = message;
        this.expeditions = new ArrayList<>();
    }
    public ExpeditionsForCompanyDTO(MessageDTO message) {
        this.message = message.getMessage();
        this.expeditions = new ArrayList<>();
    }

    public ExpeditionsForCompanyDTO(String message, List<ExpeditionForCompanyDTO> expeditions) {
        this.message = message;
        this.expeditions = expeditions;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public List<ExpeditionForCompanyDTO> getExpeditions() {
        return expeditions;
    }
    public void setExpeditions(List<ExpeditionForCompanyDTO> expeditions) {
        this.expeditions = expeditions;
    }
}

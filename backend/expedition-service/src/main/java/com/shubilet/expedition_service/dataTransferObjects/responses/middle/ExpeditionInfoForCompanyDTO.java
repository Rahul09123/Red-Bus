package com.shubilet.expedition_service.dataTransferObjects.responses.middle;

import com.shubilet.expedition_service.dataTransferObjects.responses.base.ExpeditionForCompanyDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.message.MessageDTO;

public class ExpeditionInfoForCompanyDTO {

    private String message;
    private ExpeditionForCompanyDTO expedition;

    public ExpeditionInfoForCompanyDTO() {

    }

    public ExpeditionInfoForCompanyDTO(String message) {
        this.message = message;
        this.expedition = new ExpeditionForCompanyDTO(
            0,
            "",
            "",
            "",
            "",
            0.0,
            0,
            0,
            0,
            0.0
        );
    }

    public ExpeditionInfoForCompanyDTO(MessageDTO messageDTO) {
        this.message = messageDTO.getMessage();
        this.expedition = new ExpeditionForCompanyDTO(
            0,
            "",
            "",
            "",
            "",
            0.0,
            0,
            0,
            0,
            0.0
        );
    }

    public ExpeditionInfoForCompanyDTO(String message, ExpeditionForCompanyDTO expedition) {
        this.message = message;
        this.expedition = expedition;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public ExpeditionForCompanyDTO getExpedition() {
        return expedition;
    }
    public void setExpedition(ExpeditionForCompanyDTO expedition) {
        this.expedition = expedition;
    }
}
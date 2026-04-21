package com.shubilet.api_gateway.dataTransferObjects.external.requests.expeditionOperations;

public class ExpeditionIdDTO {
    private int expeditionId;

    public ExpeditionIdDTO() {

    }

    public ExpeditionIdDTO(int expeditionId) {
        this.expeditionId = expeditionId;
    }

    public int getExpeditionId() {
        return expeditionId;
    }

    public void setExpeditionId(int expeditionId) {
        this.expeditionId = expeditionId;
    }
}

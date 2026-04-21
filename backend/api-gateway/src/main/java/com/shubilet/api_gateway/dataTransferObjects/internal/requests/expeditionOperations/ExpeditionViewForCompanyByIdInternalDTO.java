package com.shubilet.api_gateway.dataTransferObjects.internal.requests.expeditionOperations;

public class ExpeditionViewForCompanyByIdInternalDTO {
    private int companyId;
    private int expeditionId;

    public ExpeditionViewForCompanyByIdInternalDTO() {

    }

    public ExpeditionViewForCompanyByIdInternalDTO(int companyId, int expeditionId) {
        this.companyId = companyId;
        this.expeditionId = expeditionId;
    }

    public int getCompanyId() {
        return companyId;
    }
    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getExpeditionId() {
        return expeditionId;
    }
    public void setExpeditionId(int expeditionId) {
        this.expeditionId = expeditionId;
    }
}

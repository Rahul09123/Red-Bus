package com.shubilet.expedition_service.dataTransferObjects.requests;

public class ExpeditionViewByIdDTO {
    private int companyId;
    private int expeditionId;

    public ExpeditionViewByIdDTO() {
        
    }
    
    public ExpeditionViewByIdDTO(int companyId, int expeditionId) {
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

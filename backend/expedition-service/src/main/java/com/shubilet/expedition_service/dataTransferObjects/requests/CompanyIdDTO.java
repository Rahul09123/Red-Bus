package com.shubilet.expedition_service.dataTransferObjects.requests;

public class CompanyIdDTO {
    private int companyId;

    public CompanyIdDTO() {

    }

    public CompanyIdDTO(int companyId) {
        this.companyId = companyId;
    }

    public int getCompanyId() {
        return companyId;
    }
}

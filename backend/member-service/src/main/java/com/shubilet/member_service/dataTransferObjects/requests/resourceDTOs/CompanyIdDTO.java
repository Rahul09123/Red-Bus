package com.shubilet.member_service.dataTransferObjects.requests.resourceDTOs;

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

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}

package com.shubilet.api_gateway.dataTransferObjects.internal.requests;

public class CompanyIdDTO {
    private int companyId;

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

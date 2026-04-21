package com.shubilet.api_gateway.dataTransferObjects.external.requests.VerificationController;

public class CompanyVerificationExternalDTO {
    private int companyId;

    public CompanyVerificationExternalDTO() {

    }

    public CompanyVerificationExternalDTO(int companyId) {
        this.companyId = companyId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}

package com.shubilet.api_gateway.dataTransferObjects.external.requests.profileManagement;

public class FavoriteCompanyAdditionExternalDTO {
    private int companyId;

    public FavoriteCompanyAdditionExternalDTO(int customerId, int companyId) {
        this.companyId = companyId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}

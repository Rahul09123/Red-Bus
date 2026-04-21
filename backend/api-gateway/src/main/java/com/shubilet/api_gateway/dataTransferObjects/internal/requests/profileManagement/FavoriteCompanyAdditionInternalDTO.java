package com.shubilet.api_gateway.dataTransferObjects.internal.requests.profileManagement;

public class


FavoriteCompanyAdditionInternalDTO {
    private int customerId;
    private int companyId;

    public FavoriteCompanyAdditionInternalDTO(int customerId, int companyId) {
        this.customerId = customerId;
        this.companyId = companyId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}

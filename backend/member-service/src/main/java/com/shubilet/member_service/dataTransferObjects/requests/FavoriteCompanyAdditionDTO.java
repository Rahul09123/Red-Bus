package com.shubilet.member_service.dataTransferObjects.requests;

public class FavoriteCompanyAdditionDTO {
    private int customerId;
    private int companyId;

    public FavoriteCompanyAdditionDTO(int customerId, int companyId) {
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

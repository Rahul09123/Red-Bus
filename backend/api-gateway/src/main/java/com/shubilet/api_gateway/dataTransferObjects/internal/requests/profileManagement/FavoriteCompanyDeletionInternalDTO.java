package com.shubilet.api_gateway.dataTransferObjects.internal.requests.profileManagement;

public class FavoriteCompanyDeletionInternalDTO {
    private int customerId;
    private int relationId;

    public FavoriteCompanyDeletionInternalDTO(int customerId, int relationId) {
        this.customerId = customerId;
        this.relationId = relationId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getRelationId() {
        return relationId;
    }

    public void setRelationId(int relationId) {
        this.relationId = relationId;
    }
}

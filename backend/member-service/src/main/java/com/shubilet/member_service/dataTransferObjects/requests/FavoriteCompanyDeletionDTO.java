package com.shubilet.member_service.dataTransferObjects.requests;

public class FavoriteCompanyDeletionDTO {
    private int customerId;
    private int relationId;

    public FavoriteCompanyDeletionDTO(int customerId, int relationId) {
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

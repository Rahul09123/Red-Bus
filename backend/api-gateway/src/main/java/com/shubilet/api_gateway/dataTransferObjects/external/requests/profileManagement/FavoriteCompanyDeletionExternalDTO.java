package com.shubilet.api_gateway.dataTransferObjects.external.requests.profileManagement;

public class FavoriteCompanyDeletionExternalDTO {
    private int relationId;

    public FavoriteCompanyDeletionExternalDTO(int customerId, int relationId) {
        this.relationId = relationId;
    }

    public int getRelationId() {
        return relationId;
    }

    public void setRelationId(int relationId) {
        this.relationId = relationId;
    }
}

package com.shubilet.api_gateway.dataTransferObjects.internal.requests.profileManagement;

public class CardDeletionInternalDTO {
    private int customerId;
    private int cardId;

    public CardDeletionInternalDTO() {
    }

    public CardDeletionInternalDTO(int customerId, int cardId) {
        this.customerId = customerId;
        this.cardId = cardId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }
}

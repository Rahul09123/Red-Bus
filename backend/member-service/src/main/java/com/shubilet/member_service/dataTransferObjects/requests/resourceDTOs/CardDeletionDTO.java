package com.shubilet.member_service.dataTransferObjects.requests.resourceDTOs;

public class CardDeletionDTO  {
    private int cardId;
    private int customerId;

    CardDeletionDTO() {

    }

    CardDeletionDTO(int cardId, int customerId) {
        this.cardId = cardId;
        this.customerId = customerId;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
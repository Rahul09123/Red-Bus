package com.shubilet.api_gateway.dataTransferObjects.external.requests.profileManagement;

public class CardDeletionExternalDTO {

    private int cardId;

    public CardDeletionExternalDTO() {
    }

    public CardDeletionExternalDTO(int cardId) {
        this.cardId = cardId;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }
}

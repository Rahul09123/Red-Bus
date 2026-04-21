package com.shubilet.payment_service.dataTransferObjects.requests;

import java.io.Serializable;

public class CardDeactivationRequestDTO implements Serializable {

    private Integer cardId;
    private Integer customerId;

    public CardDeactivationRequestDTO() {}

    public CardDeactivationRequestDTO(Integer cardId, Integer customerId) {
        this.cardId = cardId;
        this.customerId = customerId;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
}
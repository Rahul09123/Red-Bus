package com.shubilet.payment_service.dataTransferObjects.requests;

import java.io.Serializable;

public class CardIdRequestDTO implements Serializable {

    private Integer cardId;

    public CardIdRequestDTO() {
    }

    public CardIdRequestDTO(Integer cardId) {
        this.cardId = cardId;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }
}
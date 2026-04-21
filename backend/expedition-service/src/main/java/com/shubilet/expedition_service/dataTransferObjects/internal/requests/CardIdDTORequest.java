package com.shubilet.expedition_service.dataTransferObjects.internal.requests;

import java.io.Serializable;

public class CardIdDTORequest implements Serializable{
    private Integer cardId;

    public CardIdDTORequest() {
    }

    public CardIdDTORequest(Integer cardId) {
        this.cardId = cardId;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }
}

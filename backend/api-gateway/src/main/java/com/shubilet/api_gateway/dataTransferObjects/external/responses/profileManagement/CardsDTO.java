package com.shubilet.api_gateway.dataTransferObjects.external.responses.profileManagement;

import java.util.ArrayList;
import java.util.List;

public class CardsDTO {
    private String message;
    private List<CardDTO> cards;

    public CardsDTO() {

    }

    public CardsDTO(String message) {
        this.message = message;
        cards = new ArrayList<>();

    }

    public CardsDTO(String message, List<CardDTO> cards) {
        this.message = message;
        this.cards = cards;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public List<CardDTO> getCards() {
        return cards;
    }
    public void setCards(List<CardDTO> cards) {
        this.cards = cards;
    }
}

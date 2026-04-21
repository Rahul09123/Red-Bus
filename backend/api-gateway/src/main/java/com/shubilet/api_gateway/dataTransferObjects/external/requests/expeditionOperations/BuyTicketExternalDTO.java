package com.shubilet.api_gateway.dataTransferObjects.external.requests.expeditionOperations;

public class BuyTicketExternalDTO {
    private int expeditionId;
    private int seatNo;
    private int cardId;

    public BuyTicketExternalDTO() {

    }

    public BuyTicketExternalDTO(int expeditionId, int seatNo, int cardId) {
        this.expeditionId = expeditionId;
        this.seatNo = seatNo;
        this.cardId = cardId;
    }

    public int getExpeditionId() {
        return expeditionId;
    }

    public void setExpeditionId(int expeditionId) {
        this.expeditionId = expeditionId;
    }

    public int getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(int seatNo) {
        this.seatNo = seatNo;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

}

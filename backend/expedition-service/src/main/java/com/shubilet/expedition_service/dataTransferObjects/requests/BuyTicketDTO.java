package com.shubilet.expedition_service.dataTransferObjects.requests;

public class BuyTicketDTO {
    private int customerId;
    private int expeditionId;
    private int seatNo;
    private int cardId;

    public BuyTicketDTO() {

    }

    public BuyTicketDTO(int customerId, int expeditionId, int seatNo, int cardId) {
        this.customerId = customerId;
        this.expeditionId = expeditionId;
        this.seatNo = seatNo;
        this.cardId = cardId;
    }

    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

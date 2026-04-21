package com.shubilet.api_gateway.dataTransferObjects.external.responses.profileManagement;

public class CardDTO {
    private int cardId;
    private String cardNo;
    private String expirationMonth;
    private String expirationYear;

    public CardDTO() {

    }

    public CardDTO(
        int cardId, 
        String cardNo, 
        String expirationMonth, 
        String expirationYear
    ) {
        this.cardId = cardId;
        this.cardNo = cardNo;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
    }

    public CardDTO(
        Integer cardId, 
        String cardNo, 
        String expirationMonth,
        String expirationYear
    ) {
        this.cardId = cardId;
        this.cardNo = cardNo;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
    }

    public int getCardId() {
        return cardId;
    }
    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getCardNo() {
        return cardNo;
    }
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }
    public void setExpirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }
    public void setExpirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
    }

}

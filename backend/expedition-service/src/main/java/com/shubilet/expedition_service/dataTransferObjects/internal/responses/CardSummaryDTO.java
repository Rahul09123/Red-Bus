package com.shubilet.expedition_service.dataTransferObjects.internal.responses;


public class CardSummaryDTO {

    private String cardId;
    private String last4Digits;
    private String expirationMonth;
    private String expirationYear;

    public CardSummaryDTO() {}

    public CardSummaryDTO(String cardId,
                          String last4Digits,
                          String expirationMonth,
                          String expirationYear) {
        this.cardId = cardId;
        this.last4Digits = last4Digits;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
    }

    public String getCardId() {
        return cardId;
    }
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getLast4Digits() {
        return last4Digits;
    }
    public void setLast4Digits(String last4Digits) {
        this.last4Digits = last4Digits;
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

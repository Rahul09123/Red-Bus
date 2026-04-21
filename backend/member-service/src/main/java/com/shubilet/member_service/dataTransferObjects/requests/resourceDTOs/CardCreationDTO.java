package com.shubilet.member_service.dataTransferObjects.requests.resourceDTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class CardCreationDTO implements Serializable {

    @NotBlank(message = "Card holder name cannot be empty")
    @Size(min = 2, max = 50, message = "Card holder name must be between 2 and 50 characters")
    private String cardHolderName;

    @NotBlank(message = "Card number cannot be empty")
    @Pattern(regexp = "\\d{16}", message = "Card number must be exactly 16 digits")
    private String cardNumber;

    @NotBlank(message = "Expiration month cannot be empty")
    @Pattern(regexp = "(0[1-9]|1[0-2])", message = "Month must be between 01 and 12")
    private String expirationMonth;

    @NotBlank(message = "Expiration year cannot be empty")
    @Pattern(regexp = "\\d{2}", message = "Year must be 2 digits (e.g. 25)")
    private String expirationYear;

    @NotBlank(message = "CVC cannot be empty")
    @Pattern(regexp = "\\d{3}", message = "CVC must be exactly 3 digits")
    private String cvc;

    // CustomerID null olamaz
    private Integer customerId;

    public CardCreationDTO() {}

    // Getter ve Setterlar
    public String getCardHolderName() { return cardHolderName; }
    public void setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getExpirationMonth() { return expirationMonth; }
    public void setExpirationMonth(String expirationMonth) { this.expirationMonth = expirationMonth; }

    public String getExpirationYear() { return expirationYear; }
    public void setExpirationYear(String expirationYear) { this.expirationYear = expirationYear; }

    public String getCvc() { return cvc; }
    public void setCvc(String cvc) { this.cvc = cvc; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
}
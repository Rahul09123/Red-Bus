package com.shubilet.payment_service.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "cards")
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Not: Gerçek hayatta kart numarası şifreli veya token olarak saklanmalıdır.
    @NotBlank(message = "Card number cannot be empty")
    @Pattern(regexp = "\\d{16}", message = "Card number must be exactly 16 digits")
    @Column(name = "card_no", nullable = false, length = 16) // unique=true kaldırdım, aynı kartı silip tekrar eklemek isterse patlamasın
    private String cardNo;

    @NotBlank(message = "Expiration date cannot be empty")
    @Pattern(
        regexp = "(0[1-9]|1[0-2])/([0-9]{2})",
        message = "Expiration Date must be in MM/YY format"
    )
    @Column(name = "expiration_date", nullable = false, length = 5)
    private String expirationDate; // İsim düzeltildi: expriationDate -> expirationDate

    // Not: CVC veritabanında saklanmaz (PCI-DSS). Eğitim amaçlı bırakıldı.
    @NotBlank(message = "CVC cannot be empty")
    @Pattern(regexp = "\\d{3}", message = "CVC must be exactly 3 digits")
    @Column(nullable = false, length = 3)
    private String cvc; // İsim düzeltildi: CVC -> cvc (Java naming convention)

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Surname cannot be empty")
    @Size(max = 100, message = "Surname cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String surname;

    @NotNull(message = "Customer ID is required")
    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @NotNull(message = "Active status must be specified")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public Card() {
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCardNo() { return cardNo; }
    public void setCardNo(String cardNo) { this.cardNo = cardNo; }

    public String getExpirationDate() { return expirationDate; }
    public void setExpirationDate(String expirationDate) { this.expirationDate = expirationDate; }

    public String getCvc() { return cvc; }
    public void setCvc(String cvc) { this.cvc = cvc; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public String getMaskedCardNo() {
        if (cardNo == null || cardNo.length() < 4) return cardNo;
        return "**** **** **** " + cardNo.substring(cardNo.length() - 4);
    }
}
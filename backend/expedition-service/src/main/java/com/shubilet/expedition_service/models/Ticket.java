package com.shubilet.expedition_service.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

/**
 * Represents a ticket record that links a seat and a payment.
 * Each ticket has a unique PNR code and is tied to one seat and one payment.
 */
@Entity
@Table(name = "tickets")
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    // ------------------------
    // Primary Key
    // ------------------------
    @Id
    @NotBlank
    @Column(nullable = false, updatable = false, unique = true)
    private String PNR;

    @NotNull
    @Column(name = "seat_id", nullable = false, updatable = true)
    private Integer seatId;

    @NotNull
    @Column(name = "payment_id", nullable = false, updatable = false)
    private Integer paymentId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "customer_id", nullable = false, updatable = false)
    private Integer customerId;

    // ------------------------
    // Constructors
    // ------------------------
    public Ticket() {
    }

    public Ticket(String PNR, Integer seatId, Integer paymentId, Integer customerId) {
        this.PNR = PNR;
        this.seatId = seatId;
        this.paymentId = paymentId;
        this.customerId = customerId;
    }

    // ------------------------
    // Lifecycle Callbacks
    // ------------------------
    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    // ------------------------
    // Getters and Setters
    // ------------------------

    public String getPNR() {
        return PNR;
    }
    public void setPNR(String PNR) {
        this.PNR = PNR;
    }

    public Integer getSeatId() {
        return seatId;
    }
    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
    }

    public Integer getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    // ------------------------
    // Equality & HashCode
    // ------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;
        Ticket ticket = (Ticket) o;
        return PNR.equals(ticket.PNR);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(PNR);
    }

    // ------------------------
    // String Representation
    // ------------------------
    @Override
    public String toString() {
        return "Ticket{" +
                ", PNR='" + PNR + '\'' +
                ", seatId=" + seatId +
                ", paymentId=" + paymentId +
                ", createdAt=" + createdAt +
                '}';
    }
}

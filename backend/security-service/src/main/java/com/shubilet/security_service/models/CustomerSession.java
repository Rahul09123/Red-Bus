package com.shubilet.security_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

/**

    Domain: Persistence

    Represents the JPA entity used to persist and manage session records for customer users
    within the authentication subsystem. Each session entry binds a customer identifier to a
    unique session code, along with creation and expiration timestamps that determine the active
    lifecycle of the session. Field-level validation helps ensure data integrity, while lifecycle
    callbacks automatically initialize timestamps on persistence. Convenience methods such as
    expiration checks assist higher-level services in enforcing session validity.

    <p>

        Technologies:

        <ul>
            <li>Jakarta Persistence (JPA) for ORM mapping</li>
            <li>Hibernate Validator for field constraint enforcement</li>
            <li>Java Time API for timestamp handling</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMirliva

    @version 1.0
*/
@Entity
@Table(name = "customer_sessions")
public class CustomerSession implements Serializable {

    private static final long serialVersionUID = 1L;

    // ------------------------
    // Primary Key
    // ------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // ------------------------
    // Fields
    // ------------------------
    @NotNull
    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @NotBlank
    @Column(nullable = false, unique = true, length = 64)
    private String code;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant expiresAt;

    // ------------------------
    // Constructors
    // ------------------------
    public CustomerSession() {
    }

    public CustomerSession(Integer customerId, String code, Instant expiresAt) {
        this.customerId = customerId;
        this.code = code;
        this.expiresAt = expiresAt;
    }

    // ------------------------
    // Lifecycle Callbacks
    // ------------------------
    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        // expiresAt zaten constructor'dan gelir ama null ise 30 dk default
        if (this.expiresAt == null) {
            this.expiresAt = this.createdAt.plusSeconds(30 * 60);
        }
    }

    // ------------------------
    // Getters and Setters
    // ------------------------
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    // ------------------------
    // Helper Methods
    // ------------------------
    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    // ------------------------
    // Equality & HashCode
    // ------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerSession)) return false;
        CustomerSession that = (CustomerSession) o;
        return id == that.id && code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, code);
    }

    // ------------------------
    // String Representation
    // ------------------------
    @Override
    public String toString() {
        return "CustomerSession{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", code='" + code + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                '}';
    }
}

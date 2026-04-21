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

    Represents the JPA entity for storing and managing administrator session records within
    the authentication system. Each session entry maps an admin user to a uniquely generated
    session code, along with creation and expiration timestamps used for lifecycle management.
    The entity enforces field-level constraints for data integrity, applies automatic timestamp
    initialization through lifecycle callbacks, and provides helper methods for expiration checks.
    It functions as the persistence backbone for admin-side session handling in the system.

    <p>

        Technologies:

        <ul>
            <li>Jakarta Persistence (JPA) for ORM mapping</li>
            <li>Hibernate Validator annotations for field validation</li>
            <li>Java Time API for timestamp management</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMirliva

    @version 1.0
*/
@Entity
@Table(name = "admin_sessions")
public class AdminSession implements Serializable {

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
    @Column(name = "admin_id", nullable = false)
    private Integer adminId;

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
    public AdminSession() {
    }

    public AdminSession(Integer adminId, String code, Instant expiresAt) {
        this.adminId = adminId;
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

    public Integer getadminId() {
        return adminId;
    }
    public void setadminId(Integer adminId) {
        this.adminId = adminId;
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
        if (!(o instanceof AdminSession)) return false;
        AdminSession that = (AdminSession) o;
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
        return "adminSession{" +
                "id=" + id +
                ", adminId=" + adminId +
                ", code='" + code + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                '}';
    }
}

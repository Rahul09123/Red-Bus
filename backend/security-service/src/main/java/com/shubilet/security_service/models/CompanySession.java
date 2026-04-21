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

    Represents the JPA entity responsible for managing session records associated with company
    users in the authentication system. Each persisted session contains a company identifier,
    a unique session code, and timestamps used to determine creation and expiration lifecycle.
    Validation annotations ensure that essential fields meet integrity requirements, while the
    entity’s lifecycle callback initializes timestamps and enforces default expiration behavior.
    Helper methods such as expiration checks support business logic in session services.

    <p>

        Technologies:

        <ul>
            <li>Jakarta Persistence (JPA) for ORM mapping</li>
            <li>Hibernate Validator for field-level validation</li>
            <li>Java Time API for timestamp and expiration management</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMirliva

    @version 1.0
*/
@Entity
@Table(name = "company_sessions")
public class CompanySession implements Serializable {

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
    @Column(name = "company_id", nullable = false)
    private Integer companyId;

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
    public CompanySession() {
    }

    public CompanySession(Integer companyId, String code, Instant expiresAt) {
        this.companyId = companyId;
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

    public Integer getcompanyId() {
        return companyId;
    }
    public void setcompanyId(Integer companyId) {
        this.companyId = companyId;
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
        if (!(o instanceof CompanySession)) return false;
        CompanySession that = (CompanySession) o;
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
        return "companySession{" +
                "id=" + id +
                ", companyId=" + companyId +
                ", code='" + code + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                '}';
    }
}

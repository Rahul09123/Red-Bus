package com.shubilet.member_service.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.Instant;

/**
 * Represents a company within the system.
 * Companies have a name, password-based authentication, and verification status.
 */
@Entity
@Table(name = "companies")
public class Company implements Serializable {

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
    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String title;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank
    @Size(min = 8)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean isVerified = false;


    // Reference to the admin who verified this company
    @Column(name = "ref_admin_id", nullable = true)
    private Integer refAdminId;

    // ------------------------
    // Audit Fields
    // ------------------------
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @Column(nullable = true)
    private Instant verifiedAt;

    // ------------------------
    // Constructors
    // ------------------------
    public Company() {
    }

    public Company(String title, String email, String password) {
        this.title = title;
        this.email = email;
        this.password = password;
        this.isVerified = false;
    }

    // ------------------------
    // Lifecycle Callbacks
    // ------------------------
    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
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

    public String getName() {
        return title;
    }

    public void setName(String name) {
        this.title = name;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
        if (verified && this.verifiedAt == null)
            this.verifiedAt = Instant.now();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRefAdminId() {
        return refAdminId;
    }

    public void setRefAdminId(Integer refAdminId) {
        this.refAdminId = refAdminId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(Instant verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // ------------------------
    // Utility Methods
    // ------------------------
    public void verify(Integer adminId) {
        this.isVerified = true;
        this.refAdminId = adminId;
        this.verifiedAt = Instant.now();
    }

    // ------------------------
    // Equality & HashCode
    // ------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return id == company.id && java.util.Objects.equals(title, company.title);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, email);
    }

    // ------------------------
    // String Representation
    // ------------------------
    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + title + '\'' +
                ", email='" + email + '\'' +
                ", isVerified=" + isVerified +
                ", refAdminId=" + refAdminId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", verifiedAt=" + verifiedAt +
                '}';
    }
}

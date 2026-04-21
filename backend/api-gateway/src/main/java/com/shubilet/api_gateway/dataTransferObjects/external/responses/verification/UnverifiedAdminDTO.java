package com.shubilet.api_gateway.dataTransferObjects.external.responses.verification;

public class UnverifiedAdminDTO {
    private int id;
    private String name;
    private String surname;
    private String email;
    private Boolean isVerified;

    public UnverifiedAdminDTO() {

    }

    public UnverifiedAdminDTO(int id, String name, String surname, String email, Boolean isVerified) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.isVerified = isVerified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }
}

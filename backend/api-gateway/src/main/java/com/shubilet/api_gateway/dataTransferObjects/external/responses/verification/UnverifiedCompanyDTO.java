package com.shubilet.api_gateway.dataTransferObjects.external.responses.verification;

public class UnverifiedCompanyDTO {
    private int id;
    private String title;
    private String email;
    private Boolean isVerified;

    public UnverifiedCompanyDTO() {

    }

    public UnverifiedCompanyDTO(int id, String title, String email, Boolean isVerified) {
        this.id = id;
        this.title = title;
        this.email = email;
        this.isVerified = isVerified;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

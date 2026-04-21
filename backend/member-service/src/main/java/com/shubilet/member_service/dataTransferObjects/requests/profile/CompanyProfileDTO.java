package com.shubilet.member_service.dataTransferObjects.requests.profile;

public class CompanyProfileDTO {
    private String title;
    private String email;
    private String message;

    public CompanyProfileDTO() {

    }
    public CompanyProfileDTO(String message) {
        this.message = message;
    }
    public CompanyProfileDTO(String title, String email) {
        this.title = title;
        this.email = email;
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

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}

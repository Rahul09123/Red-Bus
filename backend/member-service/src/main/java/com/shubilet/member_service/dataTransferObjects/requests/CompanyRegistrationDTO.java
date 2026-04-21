package com.shubilet.member_service.dataTransferObjects.requests;


public class CompanyRegistrationDTO {
    private String title;
    private String email;
    private String password;

    CompanyRegistrationDTO() {

    }

    CompanyRegistrationDTO(String title, String email, String password) {
        this.title = title;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

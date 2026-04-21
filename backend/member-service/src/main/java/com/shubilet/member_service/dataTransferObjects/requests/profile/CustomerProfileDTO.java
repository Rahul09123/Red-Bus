package com.shubilet.member_service.dataTransferObjects.requests.profile;

public class CustomerProfileDTO {
    private String name;
    private String surname;
    private String gender;
    private String email;
    private String message;

    public CustomerProfileDTO() {

    }

    public CustomerProfileDTO(String name, String surname, String gender, String email) {
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.email = email;
    }

    public CustomerProfileDTO(String message) {
        this.message = message;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

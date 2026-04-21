package com.shubilet.member_service.dataTransferObjects.responses;


public class MemberSessionDTO {
    private int userId;
    private String userType;
    private String message;

    MemberSessionDTO() {

    }

    public MemberSessionDTO(int userId, String userType) {
        this.userId = userId;
        this.userType = userType;
    }

    public MemberSessionDTO(String message) {
        this.message = message;
        this.userId = -1;
        this.userType = null;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}

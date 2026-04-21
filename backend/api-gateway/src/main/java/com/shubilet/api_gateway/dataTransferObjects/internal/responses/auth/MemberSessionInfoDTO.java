package com.shubilet.api_gateway.dataTransferObjects.internal.responses.auth;


public class MemberSessionInfoDTO {
    private int userId;
    private String userType;
    private String message;

    MemberSessionInfoDTO() {

    }

    MemberSessionInfoDTO(int userId, String userType) {
        this.userId = userId;
        this.userType = userType;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

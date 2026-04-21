package com.shubilet.api_gateway.dataTransferObjects.internal.requests;


public class AdminIdDTO {
    private int adminId;

    public AdminIdDTO() {

    }

    public AdminIdDTO(int adminId) {
        this.adminId = adminId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
}

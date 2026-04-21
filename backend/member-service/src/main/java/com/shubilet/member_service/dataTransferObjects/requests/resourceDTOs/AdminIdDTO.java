package com.shubilet.member_service.dataTransferObjects.requests.resourceDTOs;

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

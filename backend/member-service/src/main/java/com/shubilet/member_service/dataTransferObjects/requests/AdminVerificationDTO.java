package com.shubilet.member_service.dataTransferObjects.requests;

public class AdminVerificationDTO {
    private int adminId;
    private int candidateAdminId;

    public AdminVerificationDTO() {

    }

    public AdminVerificationDTO(int adminId, int candidateAdminId) {
        this.adminId = adminId;
        this.candidateAdminId = candidateAdminId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public int getCandidateAdminId() {
        return candidateAdminId;
    }

    public void setCandidateAdminId(int candidateAdminId) {
        this.candidateAdminId = candidateAdminId;
    }
}

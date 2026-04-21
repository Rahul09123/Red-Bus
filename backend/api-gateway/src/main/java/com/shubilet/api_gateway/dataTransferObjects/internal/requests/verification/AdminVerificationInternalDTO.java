package com.shubilet.api_gateway.dataTransferObjects.internal.requests.verification;

public class AdminVerificationInternalDTO {
    private int adminId;
    private int candidateAdminId;

    public AdminVerificationInternalDTO() {

    }

    public AdminVerificationInternalDTO(int adminId, int candidateAdminId) {
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

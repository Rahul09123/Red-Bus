package com.shubilet.api_gateway.dataTransferObjects.external.requests.VerificationController;

public class AdminVerificationExternalDTO {
    private int candidateAdminId;

    public AdminVerificationExternalDTO() {

    }

    public AdminVerificationExternalDTO(int candidateAdminId) {
        this.candidateAdminId = candidateAdminId;
    }


    public int getCandidateAdminId() {
        return candidateAdminId;
    }

    public void setCandidateAdminId(int candidateAdminId) {
        this.candidateAdminId = candidateAdminId;
    }
}

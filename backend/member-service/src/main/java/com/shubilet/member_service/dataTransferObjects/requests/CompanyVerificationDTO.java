package com.shubilet.member_service.dataTransferObjects.requests;

public class CompanyVerificationDTO {
    private int adminId;
    private int candidateCompanyId;

    public CompanyVerificationDTO() {

    }

    public CompanyVerificationDTO(int adminId, int candidateCompanyId) {
        this.adminId = adminId;
        this.candidateCompanyId = candidateCompanyId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public int getCandidateCompanyId() {
        return candidateCompanyId;
    }

    public void setCandidateCompanyId(int candidateCompanyId) {
        this.candidateCompanyId = candidateCompanyId;
    }

}

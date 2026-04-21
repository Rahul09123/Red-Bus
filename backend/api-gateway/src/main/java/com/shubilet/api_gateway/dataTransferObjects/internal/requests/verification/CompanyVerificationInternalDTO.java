package com.shubilet.api_gateway.dataTransferObjects.internal.requests.verification;

public class CompanyVerificationInternalDTO {
    private int adminId;
    private int candidateCompanyId;

    public CompanyVerificationInternalDTO() {

    }
    public CompanyVerificationInternalDTO(int adminId, int candidateCompanyId) {
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

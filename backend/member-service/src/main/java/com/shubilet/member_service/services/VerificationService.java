package com.shubilet.member_service.services;

import com.shubilet.member_service.dataTransferObjects.responses.UnverifiedAdminsDTO;
import com.shubilet.member_service.dataTransferObjects.responses.UnverifiedCompaniesDTO;

public interface VerificationService {
    boolean hasClearance(int adminId);

    boolean isAdminExists(int adminId);

    boolean isCompanyExists(int adminId);

    boolean markCompanyVerified(int adminId, int candidateCompanyId);

    boolean markAdminVerified(int adminId, int candidateAdminId);

    UnverifiedAdminsDTO getUnverifiedAdmins();

    UnverifiedCompaniesDTO getUnverifiedCompanies();
}


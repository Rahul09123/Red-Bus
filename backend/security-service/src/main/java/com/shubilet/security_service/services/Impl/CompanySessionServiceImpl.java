package com.shubilet.security_service.services.Impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shubilet.security_service.common.constants.AppConstants;
import com.shubilet.security_service.common.enums.SessionStatus;
import com.shubilet.security_service.common.enums.UserType;
import com.shubilet.security_service.common.util.SessionKeyGenerator;
import com.shubilet.security_service.dataTransferObjects.CookieDTO;
import com.shubilet.security_service.dataTransferObjects.requests.StatusDTO;
import com.shubilet.security_service.models.CompanySession;
import com.shubilet.security_service.repositories.CompanySessionRepository;
import com.shubilet.security_service.services.CompanySessionService;


@Service
public class CompanySessionServiceImpl implements CompanySessionService {
    private final CompanySessionRepository companySessionRepository;

    public CompanySessionServiceImpl(CompanySessionRepository companySessionRepository) {
        this.companySessionRepository = companySessionRepository;
    }


    public ResponseEntity<CookieDTO> createSession(int companyId) {
        String code = "";

        while (true) {
            code = SessionKeyGenerator.generate();
            if (!companySessionRepository.hasCode(code)) {
                break;
            }
        }

        CompanySession companySession = new CompanySession(companyId, code, AppConstants.DEFAULT_SESSION_EXPIRATION_DURATION);

        companySessionRepository.save(companySession);

        return ResponseEntity.ok(new CookieDTO(String.valueOf(companyId), UserType.COMPANY.getCode(), code));
    }

    /**

        Operation: Logout

        Performs a logout operation by validating whether the session entry
        exists for the given identifier and removing it from the underlying
        persistence store. Returns a boolean indicator showing whether the
        logout process was successfully completed.

        <p>

            Uses:

            <ul>
                <li>CompanySessionRepository for session existence check and deletion</li>
            </ul>

        </p>

        @param id the unique identifier of the session to be terminated

        @return a response entity containing a boolean indicating logout success
    */
    public ResponseEntity<Boolean> logout(int id, String code) {
        CompanySession companySession = companySessionRepository.findByCompanyIdAndCode(id, code).orElse(null);

        if (companySession == null) {
            return ResponseEntity.status(404).body(false);
        }
        
        companySessionRepository.deleteByCompanyIdAndCode(id, companySession.getCode());
        return ResponseEntity.ok(true);
    }

    /**

        Operation: Validate

        Validates the company session by checking the existence of a matching
        session record, evaluating expiration status, and confirming whether
        the associated company has completed verification. Returns a structured
        status response reflecting the current session state and any validation
        failures.

        <p>

            Uses:

            <ul>
                <li>CompanySessionRepository for existence check, expiration evaluation, and company verification lookup</li>
            </ul>

        </p>

        @param companyId the identifier of the company whose session is being validated

        @param code the session code used to locate and verify the session record

        @return a response entity containing the session status result
    */
    public ResponseEntity<StatusDTO> check(int companyId, String code) {

        if(!companySessionRepository.existsByCompanyIdAndCode(companyId, code)) {
            return ResponseEntity.badRequest().body(new StatusDTO(SessionStatus.NOT_FOUND));
        }

        if(companySessionRepository.isExpired(companyId, code)) {
            return ResponseEntity.badRequest().body(new StatusDTO(SessionStatus.EXPIRED));
        }

        return ResponseEntity.ok(new StatusDTO(SessionStatus.VALID));
    }

    public boolean hasCompanySession(int companyId) {
        return companySessionRepository.existsByCompanyId(companyId);
    }

    public void cleanAllSessions() {
        companySessionRepository.deleteAll();
    }

    public void cleanExpiredSessions() {
        companySessionRepository.deleteExpiredSessions();
    }
}

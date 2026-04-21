package com.shubilet.security_service.services.Impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shubilet.security_service.dataTransferObjects.CookieDTO;
import com.shubilet.security_service.dataTransferObjects.requests.StatusDTO;
import com.shubilet.security_service.models.AdminSession;
import com.shubilet.security_service.repositories.AdminSessionRepository;
import com.shubilet.security_service.services.AdminSessionService;
import com.shubilet.security_service.common.constants.AppConstants;
import com.shubilet.security_service.common.enums.SessionStatus;
import com.shubilet.security_service.common.enums.UserType;
import com.shubilet.security_service.common.util.SessionKeyGenerator;

@Service
public class AdminSessionServiceImpl implements AdminSessionService {
    private final AdminSessionRepository adminSessionRepository;

    public AdminSessionServiceImpl(AdminSessionRepository adminSessionRepository) {
        this.adminSessionRepository = adminSessionRepository;
    }
    
    public ResponseEntity<CookieDTO> createSession(int adminId) {
        String code = "";

        while (true) {
            code = SessionKeyGenerator.generate();
            if (!adminSessionRepository.hasCode(code)) {
                break;
            }
        }

        AdminSession adminSession = new AdminSession(adminId, code, AppConstants.DEFAULT_SESSION_EXPIRATION_DURATION);

        adminSessionRepository.save(adminSession);

        return ResponseEntity.ok(new CookieDTO(String.valueOf(adminId), UserType.ADMIN.getCode(), code));
    }

    /**

        Operation: Logout

        Handles the administrator logout process by validating the existence of the session record
        associated with the provided identifier. If a matching session exists, it is removed from
        persistence, effectively terminating the admin’s authenticated state. When no such session
        exists, the method returns a 404 response indicating that the session could not be found.

        <p>

            Uses:

            <ul>
                <li>{@code adminSessionRepository} for existence checks and deletion of session records</li>
            </ul>
        </p>

        @param id the identifier of the admin session to be terminated

        @return a response entity containing {@code true} when logout succeeds, or a 404 response

        with {@code false} when the session does not exist
    */
    public ResponseEntity<Boolean> logout(int id, String code) {
        AdminSession adminSession = adminSessionRepository.findByAdminIdAndCode(id, code).orElse(null);

        if (adminSession == null) {
            return ResponseEntity.status(404).body(false);
        }

        adminSessionRepository.deleteByAdminIdAndCode(id, adminSession.getCode());
        return ResponseEntity.ok(true);
    }

    /**

        Operation: Validate

        Validates the current administrator session by checking its existence, expiration state,
        and verification status. The method first ensures that a session matching the provided
        admin identifier and session code exists. It then evaluates whether the session has
        expired and finally confirms that the administrator account is verified. Each failure
        condition results in a structured {@code StatusDTO} describing the specific session
        status issue.

        <p>

            Uses:

            <ul>
                <li>{@code adminSessionRepository} for existence checks, expiration evaluation, and admin verification</li>
                <li>{@code StatusDTO} and {@code SessionStatus} for structured session-state reporting</li>
            </ul>

        </p>

        @param adminId the administrator’s identifier associated with the session

        @param code the session code that uniquely identifies the admin session

        @return a response entity containing a {@code StatusDTO} indicating whether the session

        is valid, expired, unverified, or not found
    */
    public ResponseEntity<StatusDTO> check(int adminId, String code) {

        if(!adminSessionRepository.existsByAdminIdAndCode(adminId, code)) {
            return ResponseEntity.badRequest().body(new StatusDTO(SessionStatus.NOT_FOUND));
        }

        if(adminSessionRepository.isExpired(adminId, code)) {
            return ResponseEntity.badRequest().body(new StatusDTO(SessionStatus.EXPIRED));
        }

        return ResponseEntity.ok(new StatusDTO(SessionStatus.VALID));
    }

    public boolean hasAdminSession(int adminId) {
        return adminSessionRepository.existsByAdminId(adminId);
    }

    public void cleanAllSessions() {
        adminSessionRepository.deleteAll();
    }

    public void cleanExpiredSessions() {
        adminSessionRepository.deleteExpiredSessions();
    }
}
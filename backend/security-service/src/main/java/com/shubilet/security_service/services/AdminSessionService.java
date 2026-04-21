package com.shubilet.security_service.services;

import org.springframework.http.ResponseEntity;

import com.shubilet.security_service.dataTransferObjects.CookieDTO;
import com.shubilet.security_service.dataTransferObjects.requests.StatusDTO;

public interface AdminSessionService {
    
    public ResponseEntity<CookieDTO> createSession(int adminId);

    public ResponseEntity<Boolean> logout(int id, String code);

    /**

        Operation: Validate

        Validates an admin session by verifying that a session associated with the given
        identifier and token exists and is still active. The result is returned as a
        structured {@code StatusDTO} indicating whether the session is valid or the reason
        for failure, such as missing or expired session data.

        <p>

            Uses:

            <ul>
                <li>AdminSessionRepository for session existence checking and expiration evaluation</li>
            </ul>

        </p>

        @param id the identifier of the admin whose session is being validated
        @param token the session token used to verify the session record

        @return a response entity containing the session validation status
    */
    public ResponseEntity<StatusDTO> check(int id, String token);
    
    
    public boolean hasAdminSession(int adminId);

    /**

        Operation: Clean All Sessions

        Purges all existing admin session records from the underlying data store.
        This operation is typically used for maintenance tasks, testing scenarios,
        or administrative actions that require a complete reset of session data.

        <p>

            Uses:

            <ul>
                <li>AdminSessionRepository for bulk deletion of session records</li>
            </ul>

        </p>
    */
    public void cleanAllSessions();
    
    /**

        Operation: Clean Expired Sessions

        Removes all admin session records that have surpassed their expiration timestamps
        from the underlying data store. This operation helps maintain data integrity and
        optimizes storage by eliminating stale session entries.

        <p>

            Uses:

            <ul>
                <li>AdminSessionRepository for identifying and deleting expired session records</li>
            </ul>

        </p>
    */
    public void cleanExpiredSessions();
}

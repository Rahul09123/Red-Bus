package com.shubilet.security_service.services.Impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shubilet.security_service.common.constants.AppConstants;
import com.shubilet.security_service.common.enums.SessionStatus;
import com.shubilet.security_service.common.enums.UserType;
import com.shubilet.security_service.common.util.SessionKeyGenerator;
import com.shubilet.security_service.dataTransferObjects.CookieDTO;
import com.shubilet.security_service.dataTransferObjects.requests.StatusDTO;
import com.shubilet.security_service.models.CustomerSession;
import com.shubilet.security_service.repositories.CustomerSessionRepository;
import com.shubilet.security_service.services.CustomerSessionService;


@Service
public class CustomerSessionServiceImpl implements CustomerSessionService {
    private final CustomerSessionRepository customerSessionRepository;

    public CustomerSessionServiceImpl(CustomerSessionRepository customerSessionRepository) {
        this.customerSessionRepository = customerSessionRepository;
    }

    public ResponseEntity<CookieDTO> createSession(int customerId) {
        String code = "";

        while (true) {
            code = SessionKeyGenerator.generate();
            if (!customerSessionRepository.hasCode(code)) {
                break;
            }
        }

        CustomerSession customerSession = new CustomerSession(customerId, code, AppConstants.DEFAULT_SESSION_EXPIRATION_DURATION);

        customerSessionRepository.save(customerSession);

        return ResponseEntity.ok(new CookieDTO(String.valueOf(customerId), UserType.CUSTOMER.getCode(), code));
    }

    /**

        Operation: Logout

        Performs a logout operation by verifying the existence of a session associated
        with the given identifier and removing it from the persistence layer. Returns
        a boolean value indicating whether the logout was successfully completed,
        responding with HTTP 404 when the session does not exist.

        <p>

            Uses:

            <ul>
                <li>CustomerSessionRepository for session existence verification and deletion</li>
            </ul>

        </p>

        @param id the unique identifier of the session to be terminated

        @return a response entity containing a boolean result indicating logout success
    */
    public ResponseEntity<Boolean> logout(int id, String code) {
        CustomerSession customerSession = customerSessionRepository.findByCustomerIdAndCode(id, code).orElse(null);

        if (customerSession == null) {
            return ResponseEntity.status(404).body(false);
        }
        
        customerSessionRepository.deleteByCustomerIdAndCode(id, customerSession.getCode());
        return ResponseEntity.ok(true);
    }

    /**

        Operation: Validate

        Checks the validity of a customer session by confirming the existence of a matching
        session record and verifying whether the session has expired. Returns an appropriate
        status response indicating whether the session is valid or the specific reason for
        failure.

        <p>

            Uses:

            <ul>
                <li>CustomerSessionRepository for existence validation and expiration checks</li>
            </ul>

        </p>

        @param adminId the identifier of the customer whose session is being validated

        @param code the session code used to verify the session record

        @return a response entity containing the session validation status
    */
    public ResponseEntity<StatusDTO> check(int adminId, String code) {

        if(!customerSessionRepository.existsByCustomerIdAndCode(adminId, code)) {
            return ResponseEntity.badRequest().body(new StatusDTO(SessionStatus.NOT_FOUND));
        }

        if(customerSessionRepository.isExpired(adminId, code)) {
            return ResponseEntity.badRequest().body(new StatusDTO(SessionStatus.EXPIRED));
        }

        return ResponseEntity.ok(new StatusDTO(SessionStatus.VALID));
    }

    public boolean hasCustomerSession(int customerId) {
        return customerSessionRepository.existsByCustomerId(customerId);
    }

    public void cleanAllSessions() {
        customerSessionRepository.deleteAll();
    }

    public void cleanExpiredSessions() {
        customerSessionRepository.deleteExpiredSessions();
    }
}

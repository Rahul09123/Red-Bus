package com.shubilet.security_service.controllers.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// Mirliva says: Authentication is just trust issues implemented in Java.

import com.shubilet.security_service.common.constants.SessionKeys;
import com.shubilet.security_service.common.enums.SessionStatus;
import com.shubilet.security_service.common.enums.UserType;
import com.shubilet.security_service.common.util.ErrorUtils;
import com.shubilet.security_service.common.util.StringUtils;
import com.shubilet.security_service.common.util.ValidationUtils;
import com.shubilet.security_service.controllers.AuthController;
import com.shubilet.security_service.dataTransferObjects.CookieDTO;
import com.shubilet.security_service.dataTransferObjects.requests.LoginDTO;
import com.shubilet.security_service.dataTransferObjects.requests.StatusDTO;
import com.shubilet.security_service.dataTransferObjects.responses.CheckMessageDTO;
import com.shubilet.security_service.dataTransferObjects.responses.MessageDTO;
import com.shubilet.security_service.mapper.ResponseEntityMapper;
import com.shubilet.security_service.services.AdminSessionService;
import com.shubilet.security_service.services.CompanySessionService;
import com.shubilet.security_service.services.CustomerSessionService;

/****

    Domain: Authentication

    Exposes REST endpoints to manage authentication session lifecycle for multiple user domains (ADMIN, COMPANY, CUSTOMER).
    This controller coordinates session creation, logout, and session validity checks by delegating user-type-specific
    operations to dedicated session services. It applies defensive validation of incoming request payloads and cookie-backed
    session attributes, normalizes failures into consistent DTO-based responses, and proactively clears session attributes
    on invalid, expired, or inconsistent states to prevent reuse of stale authentication data.

    <p>

        Technologies:

        <ul>
            <li>Spring Web</li>
            <li>SLF4J</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @see AdminSessionService
    @see CompanySessionService
    @see CustomerSessionService

    @version 2.0
*/
@RestController
@RequestMapping("/api/auth")
public class AuthControllerImpl implements AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthControllerImpl.class);

    private final AdminSessionService adminSessionService;
    private final CompanySessionService companySessionService;
    private final CustomerSessionService customerSessionService;

    public AuthControllerImpl(AdminSessionService adminSessionService,
                            CompanySessionService companySessionService,
                            CustomerSessionService customerSessionService
                        ) {
        this.adminSessionService = adminSessionService;
        this.companySessionService = companySessionService;
        this.customerSessionService = customerSessionService;
    }

    /****

        Operation: CreateSession

        Creates an authenticated session for a user by validating the incoming {@link LoginDTO} payload and
        ensuring no active session already exists within the provided {@link CookieDTO}. Based on the provided user type,
        delegates session creation to the corresponding session service and persists the resulting identifiers and
        authorization code into the cookie-backed session attributes. Returns a {@link MessageDTO} containing the updated
        cookie context and a success message, or an error response when validation or session creation fails.

        <p>

            Uses:

            <ul>
                <li>{@link ErrorUtils} for building {@link MessageDTO}-based error responses</li>
                <li>{@link CookieDTO} as a cookie/session carrier for userId, userType, and authCode</li>
                <li>{@link SessionKeys} for session attribute key management</li>
                <li>{@link StringUtils} for null/blank checks</li>
                <li>{@link ValidationUtils} for user type validation</li>
                <li>{@link UserType} for resolving the user type code to an enum</li>
                <li>Session services{@link AdminSessionService}, {@link CompanySessionService}, {@link CustomerSessionService} (admin/company/customer) 
                    for user-type-specific session creation</li>
                <li>{@link Logger} for audit and diagnostic logging</li>
            </ul>

        </p>

        @param loginDTO the login request containing the cookie/session carrier, user identifier, and user type code

        @return a response entity containing a {@link MessageDTO} with the updated {@link CookieDTO} and a business message,
        or an error response when the session is invalid, already active, or cannot be created
    */
    @PostMapping("/createSession")
    public ResponseEntity<MessageDTO> createSession(@RequestBody LoginDTO loginDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.MESSAGE_DTO);

        CookieDTO session = loginDTO.getCookie();

        //STEP 1: Classic Validations
        if(session == null) {
            logger.warn("Login failed due to missing session");
            return errorUtils.invalidSession(session);
        }

        String userIdCookie = (String) session.getAttribute(SessionKeys.USER_ID);
        String userTypeCookie = (String) session.getAttribute(SessionKeys.USER_TYPE);
        String authCodeCookie = (String) session.getAttribute(SessionKeys.AUTH_CODE);

        if( !StringUtils.isNullOrBlank(userIdCookie) &&
            !StringUtils.isNullOrBlank(userTypeCookie) &&
            !StringUtils.isNullOrBlank(authCodeCookie)
        ) {
            clearSession(session);
            logger.warn("Login failed: user already logged in with userId {}", userIdCookie);
            return errorUtils.userAlreadyLoggedIn(session);
        }

        int userId = loginDTO.getUserId();
        String userType = loginDTO.getUserType();

        if(userId <= 0) {
            logger.warn("Login failed due to invalid userId {}", userId);
            return errorUtils.isInvalidFormat(session, String.valueOf(userId));
        }

        if(StringUtils.isNullOrBlank(userType)) {
            logger.warn("Login failed due to missing userType for userId {}", userId);
            return errorUtils.isInvalidFormat(session, "userType");
        }

        //STEP 2: Specific Validations
        
        if(!ValidationUtils.isValidUserType(userType)) {
            logger.warn("Login failed due to invalid userType {} for userId {}", userType, userId);
            return errorUtils.isInvalidFormat(session, "userType");
        }
        
        //STEP 3: Logical Processing

        UserType userTypeEnum = UserType.fromCode(userType);
        ResponseEntity<CookieDTO> response = null;
    
        if(userTypeEnum == UserType.ADMIN) {
            response = adminSessionService.createSession(userId);
        }
        else if(userTypeEnum == UserType.COMPANY) {
            response = companySessionService.createSession(userId);
        }
        else if(userTypeEnum == UserType.CUSTOMER) {
            response = customerSessionService.createSession(userId);
        }
        else {
            logger.warn("Login failed for userId {}: user not found", userId);
            return errorUtils.notFound(session,"User");
        }

        if(response == null) {
            logger.warn("Login failed for userId {}: missing response", userId);
            return errorUtils.criticalError(session);
        }

        if(response.getStatusCode() == null) {
            logger.warn("Login failed for userId {}: missing response status code", userId);
            return errorUtils.criticalError(session);
        }

        if(response.getBody() == null) {
            logger.warn("Login failed for userId {}: missing response body", userId);
            return errorUtils.criticalError(session);
        }

        if(!response.getStatusCode().is2xxSuccessful()) {
            logger.warn("Login failed for userId {}: received non-successful status code {}", userId, response.getStatusCode());
            return errorUtils.criticalError(session);
        }

        session.setAttribute(SessionKeys.USER_ID, response.getBody().getUserId());
        session.setAttribute(SessionKeys.USER_TYPE, response.getBody().getUserType());
        session.setAttribute(SessionKeys.AUTH_CODE, response.getBody().getAuthCode());

        logger.info("Login successful for userId {} as {}", userId, userType);
        return ResponseEntity.ok().body(new MessageDTO(session, "Login successful."));
    }

    /****

        Operation: Logout

        Terminates an authenticated user session by validating the provided {@link CookieDTO} attributes and delegating
        the logout operation to the appropriate user-type-specific session service. Clears the session cookie attributes
        on invalid input, invalid auth code format, non-numeric userId, unknown user type, or when the backend session
        cannot be found/terminated. Returns a {@link MessageDTO} containing the cleared cookie context and a logout message
        upon success, or an error response describing the failure condition.

        <p>

            Uses:

            <ul>
                <li>{@link ErrorUtils} for building {@link MessageDTO}-based error responses</li>
                <li>{@link CookieDTO} as a cookie/session carrier for userId, userType, and authCode</li>
                <li>{@link StringUtils} for null/blank and numeric validations</li>
                <li>{@link ValidationUtils} for auth code/session key validation</li>
                <li>{@link UserType} for user type code comparisons</li>
                <li>Session services{@link AdminSessionService}, {@link CompanySessionService}, {@link CustomerSessionService} for user-type-specific logout processing</li>
                <li>{@link Logger} for audit and diagnostic logging</li>
            </ul>

        </p>

        @param session the cookie/session payload containing the userId, userType, and authCode to be invalidated

        @return a response entity containing a {@link MessageDTO} with the cleared {@link CookieDTO} and a business message,
        or an error response when the session is missing, malformed, not found, or cannot be terminated
    */
    @PostMapping("/logout")
    public ResponseEntity<MessageDTO> logout(@RequestBody CookieDTO session) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.MESSAGE_DTO);

        // STEP 1: Classic Validations
        if(session == null) {
            logger.warn("Logout failed due to missing session");
            return errorUtils.invalidSession(session);
        }

        String userId = session.getUserId();
        String userType = session.getUserType();
        String authCode = session.getAuthCode();

        logger.info("Logout attempt for userId {} of type {}", userId, userType);

        if( StringUtils.isNullOrBlank(userId) ||
            StringUtils.isNullOrBlank(userType) ||
            StringUtils.isNullOrBlank(authCode)
        ) {
            clearSession(session);
            logger.warn("Logout failed due to invalid session attributes");
            return errorUtils.invalidSession(session);
        }


        //STEP 2: Specific Validations
        if(!ValidationUtils.isValidSessionKey(authCode)) {
            clearSession(session);
            logger.warn("Logout failed due to invalid auth code format");
            return errorUtils.invalidSession(session);
        } 

        if(!StringUtils.isNumeric(userId)) {
            clearSession(session);
            logger.warn("Logout failed due to non-numeric userId");
            return errorUtils.invalidSession(session);
        }

        //STEP 3: Logical Processing
        if(userType.equals(UserType.ADMIN.getCode())) {
            ResponseEntity<Boolean> response = adminSessionService.logout(Integer.parseInt(userId), authCode);

            if(!response.getStatusCode().is2xxSuccessful() || response.getBody() == null || !response.getBody()) {
                clearSession(session);
                logger.warn("Logout failed for admin userId {}", userId);
                return errorUtils.sessionNotFound(session);
            }
        }
        else if(userType.equals(UserType.COMPANY.getCode())) {
            ResponseEntity<Boolean> response = companySessionService.logout(Integer.parseInt(userId), authCode);

            if(!response.getStatusCode().is2xxSuccessful() || response.getBody() == null || !response.getBody()) {
                clearSession(session);
                logger.warn("Logout failed for company userId {}", userId);
                return errorUtils.sessionNotFound(session);
            }
        }
        else if(userType.equals(UserType.CUSTOMER.getCode())) {
            ResponseEntity<Boolean> response = customerSessionService.logout(Integer.parseInt(userId), authCode);

            if(!response.getStatusCode().is2xxSuccessful() || response.getBody() == null || !response.getBody()) {
                clearSession(session);
                logger.warn("Logout failed for customer userId {}", userId);
                return errorUtils.sessionNotFound(session);
            }
        }
        else {
            clearSession(session);
            logger.warn("Logout failed due to invalid user type {}", userType);
            return errorUtils.invalidSession(session);
        }

        clearSession(session);
        logger.info("Logout successful for userId {} of type {}", userId, userType);
        return ResponseEntity.ok().body(new MessageDTO(session, "Logout successful."));
    }

    /****

        Operation: Check

        Verifies whether the provided {@link CookieDTO} represents a valid and active authenticated session by performing
        defensive validation of required attributes (userId, userType, authCode) and delegating the verification to the
        appropriate user-type-specific session service. Clears the session cookie attributes when the payload is malformed,
        when the auth code format is invalid, when the userId is not numeric, or when the user type is unknown. Returns a
        {@link MessageDTO} indicating session validity on success, or an error response when the session is missing, invalid,
        expired, or cannot be confirmed.

        <p>

            Uses:

            <ul>
                <li>{@link ErrorUtils} for building {@link MessageDTO}-based error responses</li>
                <li>{@link CookieDTO} as a cookie/session carrier for userId, userType, and authCode</li>
                <li>{@link StringUtils} for null/blank checks and numeric validation</li>
                <li>{@link ValidationUtils} for session key/auth code format validation</li>
                <li>{@link UserType} for user type code comparisons</li>
                <li>{@link StatusDTO} as the backend session status payload returned by session services</li>
                <li>{@code validateSessionStatus(...)} for normalizing backend status checks into {@link MessageDTO} responses</li>
                <li>Session services{@link AdminSessionService}, {@link CompanySessionService}, {@link CustomerSessionService} (admin/company/customer) for user-type-specific session verification</li>
                <li>{@link Logger} for audit and diagnostic logging</li>
            </ul>

        </p>

        @param session the cookie/session payload containing the userId, userType, and authCode to be validated

        @return a response entity containing a {@link MessageDTO} with the original/cleared {@link CookieDTO} and a business message,
        or an error response when the session is missing, malformed, expired, or cannot be validated
    */
    @PostMapping("/check")
    public ResponseEntity<MessageDTO> check(@RequestBody CookieDTO session) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.MESSAGE_DTO);

        // STEP 1: Classic Validations
        if(session == null) {
            logger.warn("Session check failed due to missing session");
            return errorUtils.sessionNotFound(session);
        }

        String userId = session.getUserId();
        String userType = session.getUserType();
        String authCode = session.getAuthCode();

        if( StringUtils.isNullOrBlank(userId) ||
            StringUtils.isNullOrBlank(userType) ||
            StringUtils.isNullOrBlank(authCode)
        ) {
            if(StringUtils.isNullOrBlank(userId) &&
                StringUtils.isNullOrBlank(userType) &&
                StringUtils.isNullOrBlank(authCode)
            ) {
                logger.warn("Session not found");
                return errorUtils.sessionNotFound(session);
            }
            else if(StringUtils.isNullOrBlank(userId)) {
                clearSession(session);
                logger.warn("Session check failed due to missing userId {}", userId);
                return errorUtils.invalidSession(session);
            }
            else if(StringUtils.isNullOrBlank(userType)) {
                clearSession(session);
                logger.warn("Session check failed due to missing userType {}", userType);
                return errorUtils.invalidSession(session);
            }
            else if(StringUtils.isNullOrBlank(authCode)) {
                clearSession(session);
                logger.warn("Session check failed due to missing authCode {}", authCode);
                return errorUtils.invalidSession(session);
            }
            else {
                clearSession(session);
                logger.warn("An unexpected error occurred during session check {} {} {}", userId, userType, authCode);
                return errorUtils.criticalError(session);
            }

        }

        if(!ValidationUtils.isValidSessionKey(authCode)) {
            clearSession(session);
            logger.warn("Session check failed due to invalid auth code format");
            return errorUtils.invalidSession(session);
        }

        if(!StringUtils.isNumeric(userId)) {
            clearSession(session);
            logger.warn("Session check failed due to non-numeric userId");
            return errorUtils.invalidSession(session);
        }

        if(userType.equals(UserType.ADMIN.getCode())) {
            ResponseEntity<StatusDTO> response = adminSessionService.check(Integer.parseInt(userId), authCode);

            ResponseEntity<MessageDTO> validationResponse = validateSessionStatus(response, session, UserType.ADMIN);

            if(!validationResponse.getStatusCode().is2xxSuccessful()) {
                logger.warn("Session check failed for admin userId {}", userId);
                return validationResponse;
            }
        }
        else if(userType.equals(UserType.COMPANY.getCode())) {
            ResponseEntity<StatusDTO> response = companySessionService.check(Integer.parseInt(userId), authCode);

            ResponseEntity<MessageDTO> validationResponse = validateSessionStatus(response, session, UserType.COMPANY);

            if(!validationResponse.getStatusCode().is2xxSuccessful()) {
                logger.warn("Session check failed for company userId {}", userId);
                return validationResponse;
            }
        }
        else if(userType.equals(UserType.CUSTOMER.getCode())) {
            ResponseEntity<StatusDTO> response = customerSessionService.check(Integer.parseInt(userId), authCode);

            ResponseEntity<MessageDTO> validationResponse = validateSessionStatus(response, session, UserType.CUSTOMER);

            if(!validationResponse.getStatusCode().is2xxSuccessful()) {
                logger.warn("Session check failed for customer userId {}", userId);
                return validationResponse;
            }
        }
        else {
            clearSession(session);
            return errorUtils.invalidSession(session);
        }

        return ResponseEntity.ok().body(new MessageDTO(session, "Session is valid."));
    }

    /****

        Operation: CheckAdmin

        Validates that the incoming {@link CookieDTO} represents an authenticated session and specifically enforces that the
        session belongs to an ADMIN user. Reuses shared session-validation logic to ensure required attributes are present
        and consistent, then performs an admin-specific session status check via the admin session service. Maps generic
        {@link MessageDTO}-based validation outcomes into {@link CheckMessageDTO} responses and returns a payload containing
        the validated session context and the resolved admin userId when the session is confirmed as active.

        <p>

            Uses:

            <ul>
                <li>{@link ErrorUtils} for building {@link CheckMessageDTO}-based error responses</li>
                <li>{@code handleValidUserSession(...)} for common authenticated-session validation</li>
                <li>{@link SessionKeys} for retrieving userId, userType, and authCode from the session carrier</li>
                <li>{@link UserType} for enforcing ADMIN-only access</li>
                <li>{@link StatusDTO} as the backend session status payload returned by the admin session service</li>
                <li>{@code validateSessionStatus(...)} for normalizing backend status checks into {@link MessageDTO} responses</li>
                <li>{@link ResponseEntityMapper} for converting {@link MessageDTO} responses into {@link CheckMessageDTO} responses</li>
                <li>{@link AdminSessionService} for admin-specific session verification</li>
                <li>{@link Logger} for audit and diagnostic logging</li>
            </ul>

        </p>

        @param session the cookie/session payload used to resolve and validate the current authenticated session context

        @return a response entity containing a {@link CheckMessageDTO} with the session context, a business message, and the admin userId,
        or an error response when the session is invalid, not admin-scoped, expired, or cannot be validated
    */
    @PostMapping("/checkAdmin")
    public ResponseEntity<CheckMessageDTO> checkAdminSession(@RequestBody CookieDTO session) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.CHECK_MESSAGE_DTO);

        // STEP 1: Classic Validations
        ResponseEntity<MessageDTO> response = handleValidUserSession(session);
        if(!response.getStatusCode().is2xxSuccessful()) {
            logger.warn("Admin session check failed due to invalid user session");
            return ResponseEntityMapper.toCheckMessageDTOResponseEntity(response);
        }

        String userId = (String) session.getAttribute(SessionKeys.USER_ID);
        String userType = (String) session.getAttribute(SessionKeys.USER_TYPE);
        String authCode = (String) session.getAttribute(SessionKeys.AUTH_CODE);
        
        if(!userType.equals(UserType.ADMIN.getCode())) {
            logger.warn("Admin session check failed due to user type {}", userType);
            return errorUtils.invalidSession(session);
        }

        ResponseEntity<StatusDTO> checkResponse = adminSessionService.check(Integer.parseInt(userId), authCode);

        ResponseEntity<MessageDTO> validationResponse = validateSessionStatus(checkResponse, session, UserType.ADMIN);

        if(!validationResponse.getStatusCode().is2xxSuccessful()) {
            logger.warn("Admin session check failed for admin userId {}", userId);
            return ResponseEntityMapper.toCheckMessageDTOResponseEntity(validationResponse);
        }
        
        logger.info("Admin session check successful for admin userId {}", userId);
        return ResponseEntity.ok().body(new CheckMessageDTO(session, "Session is valid.", Integer.parseInt(userId)));
    }

    /****

        Operation: CheckCompany

        Validates that the incoming {@link CookieDTO} represents an authenticated session and enforces that the session
        belongs to a COMPANY user. Reuses shared session-validation logic to verify required session attributes are present
        and consistent, then delegates to the company session service to confirm the session status using the resolved
        userId and authCode. Maps generic {@link MessageDTO}-based validation outcomes into {@link CheckMessageDTO} responses
        and returns a payload containing the validated session context and the resolved company userId when the session is
        confirmed as active.

        <p>

            Uses:

            <ul>
                <li>{@link ErrorUtils} for building {@link CheckMessageDTO}-based error responses</li>
                <li>{@code handleValidUserSession(...)} for common authenticated-session validation</li>
                <li>{@link SessionKeys} for retrieving userId, userType, and authCode from the session carrier</li>
                <li>{@link UserType} for enforcing COMPANY-only access</li>
                <li>{@link StatusDTO} as the backend session status payload returned by the company session service</li>
                <li>{@code validateSessionStatus(...)} for normalizing backend status checks into {@link MessageDTO} responses</li>
                <li>{@link ResponseEntityMapper} for converting {@link MessageDTO} responses into {@link CheckMessageDTO} responses</li>
                <li>{@link CompanySessionService} for company-specific session verification</li>
                <li>{@link Logger} for audit and diagnostic logging</li>
            </ul>
            
        </p>

        @param session the cookie/session payload used to resolve and validate the current authenticated session context

        @return a response entity containing a {@link CheckMessageDTO} with the session context, a business message, and the company userId,
        or an error response when the session is invalid, not company-scoped, expired, or cannot be validated
    */
    @PostMapping("/checkCompany")
    public ResponseEntity<CheckMessageDTO> checkCompanySession(@RequestBody CookieDTO session) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.CHECK_MESSAGE_DTO);

        ResponseEntity<MessageDTO> response = handleValidUserSession(session);
        if(!response.getStatusCode().is2xxSuccessful()) {
            logger.warn("Company session check failed due to invalid user session");
            return ResponseEntityMapper.toCheckMessageDTOResponseEntity(response);
        }

        String userId = (String) session.getAttribute(SessionKeys.USER_ID);
        String userType = (String) session.getAttribute(SessionKeys.USER_TYPE);
        String authCode = (String) session.getAttribute(SessionKeys.AUTH_CODE);
        
        if(!userType.equals(UserType.COMPANY.getCode())) {
            logger.warn("Company session check failed due to user type {}", userType);
            return errorUtils.invalidSession(session);
        }

        ResponseEntity<StatusDTO> checkResponse = companySessionService.check(Integer.parseInt(userId), authCode);

        ResponseEntity<MessageDTO> validationResponse = validateSessionStatus(checkResponse, session, UserType.COMPANY);

        if(!validationResponse.getStatusCode().is2xxSuccessful()) {
            logger.warn("Company session check failed for company userId {}", userId);
            return ResponseEntityMapper.toCheckMessageDTOResponseEntity(validationResponse);
        }

        logger.info("Company session check successful for company userId {}", userId);
        return ResponseEntity.ok().body(new CheckMessageDTO(session, "Session is valid.", Integer.parseInt(userId)));
    }

    /****

        Operation: CheckCustomer

        Validates that the incoming {@link CookieDTO} represents an authenticated session and enforces that the session
        belongs to a CUSTOMER user. Reuses shared session-validation logic to verify required session attributes are present
        and consistent, then delegates to the customer session service to confirm the session status using the resolved
        userId and authCode. Maps generic {@link MessageDTO}-based validation outcomes into {@link CheckMessageDTO} responses
        and returns a payload containing the validated session context and the resolved customer userId when the session is
        confirmed as active.

        <p>

            Uses:

            <ul>
                <li>{@link ErrorUtils} for building {@link CheckMessageDTO}-based error responses</li>
                <li>{@code handleValidUserSession(...)} for common authenticated-session validation</li>
                <li>{@link SessionKeys} for retrieving userId, userType, and authCode from the session carrier</li>
                <li>{@link UserType} for enforcing CUSTOMER-only access</li>
                <li>{@link StatusDTO} as the backend session status payload returned by the customer session service</li>
                <li>{@code validateSessionStatus(...)} for normalizing backend status checks into {@link MessageDTO} responses</li>
                <li>{@link ResponseEntityMapper} for converting {@link MessageDTO} responses into {@link CheckMessageDTO} responses</li>
                <li>{@link CustomerSessionService} for customer-specific session verification</li>
                <li>{@link Logger} for audit and diagnostic logging</li>
            </ul>

        </p>

        @param session the cookie/session payload used to resolve and validate the current authenticated session context

        @return a response entity containing a {@link CheckMessageDTO} with the session context, a business message, and the customer userId,

        or an error response when the session is invalid, not customer-scoped, expired, or cannot be validated
    */
    @PostMapping("/checkCustomer")
    public ResponseEntity<CheckMessageDTO> checkCustomerSession(@RequestBody CookieDTO session) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.CHECK_MESSAGE_DTO);

        ResponseEntity<MessageDTO> response = handleValidUserSession(session);
        if(!response.getStatusCode().is2xxSuccessful()) {
            logger.warn("Customer session check failed due to invalid user session");
            return ResponseEntityMapper.toCheckMessageDTOResponseEntity(response);
        }

        String userId = (String) session.getAttribute(SessionKeys.USER_ID);
        String userType = (String) session.getAttribute(SessionKeys.USER_TYPE);
        String authCode = (String) session.getAttribute(SessionKeys.AUTH_CODE);
        
        if(!userType.equals(UserType.CUSTOMER.getCode())) {
            logger.warn("Customer session check failed due to user type {}", userType);
            return errorUtils.invalidSession(session);
        }

        ResponseEntity<StatusDTO> checkResponse = customerSessionService.check(Integer.parseInt(userId), authCode);

        ResponseEntity<MessageDTO> validationResponse = validateSessionStatus(checkResponse, session, UserType.CUSTOMER);

        if(!validationResponse.getStatusCode().is2xxSuccessful()) {
            logger.warn("Customer session check failed for customer userId {}", userId);
            return ResponseEntityMapper.toCheckMessageDTOResponseEntity(validationResponse);
        }

        logger.info("Customer session check successful for customer userId {}", userId);
        return ResponseEntity.ok().body(new CheckMessageDTO(session, "Session is valid.", Integer.parseInt(userId)));
    }



    ///HELPER METHODS START

    /****

        Operation: Clear

        Removes all authentication-related attributes from the provided {@link CookieDTO} to fully invalidate the
        current session context. This method clears the stored user identifier, user type, and authorization code
        and is typically invoked during logout, session invalidation, or error-handling flows to ensure no stale
        authentication data remains.

        <p>

            Uses:

            <ul>
                <li>{@link CookieDTO} for session attribute storage and removal</li>
                <li>{@link SessionKeys} for identifying authentication-related session attributes</li>
                <li>{@link Logger} for audit logging of session cleanup actions</li>
            </ul>
        
        </p>

        @param session the cookie/session carrier whose authentication attributes will be removed
    */
    private void clearSession(CookieDTO session) {
        session.removeAttribute(SessionKeys.USER_ID);
        session.removeAttribute(SessionKeys.USER_TYPE);
        session.removeAttribute(SessionKeys.AUTH_CODE);
        logger.info("Session attributes cleared.");
    }

    /****

        Operation: Validate

        Performs centralized validation of an authenticated user session carried by {@link CookieDTO} by ensuring the
        presence and correctness of required attributes (userId, userType, authCode). Differentiates between a completely
        missing session (no attributes present) and a malformed session (partial/invalid attributes), clearing session
        attributes on invalid states to prevent reuse of stale authentication data. Returns a successful {@link MessageDTO}
        response when the session appears structurally valid, or an error response when the session is missing or invalid.

        <p>

            Uses:

            <ul>
                <li>{@link ErrorUtils} for building {@link MessageDTO}-based error responses</li>
                <li>{@link CookieDTO} for retrieving and clearing session attributes</li>
                <li>{@link SessionKeys} for identifying authentication-related session attributes</li>
                <li>{@link StringUtils} for null/blank checks and numeric validation</li>
                <li>{@link ValidationUtils} for session key/auth code format validation</li>
                <li>{@code clearSession(...)} for removing invalid authentication attributes</li>
                <li>{@link Logger} for audit and diagnostic logging</li>
            </ul>

        </p>

        @param session the cookie/session carrier containing authentication attributes to validate

        @return a response entity containing a {@link MessageDTO} when the session is valid, or an error response when the
        session is missing, malformed, or fails format checks
    */
    private ResponseEntity<MessageDTO> handleValidUserSession(CookieDTO session) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.MESSAGE_DTO);
        if(session == null) {
            logger.warn("Session validation failed due to missing session");
            return errorUtils.sessionNotFound(session);
        }

        String userId = (String) session.getAttribute(SessionKeys.USER_ID);
        String userType = (String) session.getAttribute(SessionKeys.USER_TYPE);
        String authCode = (String) session.getAttribute(SessionKeys.AUTH_CODE);

        if( StringUtils.isNullOrBlank(userId) ||
            StringUtils.isNullOrBlank(userType) ||
            StringUtils.isNullOrBlank(authCode)
        ) {
            if(StringUtils.isNullOrBlank(userId) &&
                StringUtils.isNullOrBlank(userType) &&
                StringUtils.isNullOrBlank(authCode)
            ) {
                logger.warn("Session validation failed due to missing session attributes");
                return errorUtils.sessionNotFound(session);
            }
            else {
                clearSession(session);
                logger.warn("Session validation failed due to invalid session attributes");
                return errorUtils.invalidSession(session);
            }
        }
        else {

            if(!ValidationUtils.isValidSessionKey(authCode)) {
                clearSession(session);
                logger.warn("Session validation failed due to invalid auth code format");
                return errorUtils.invalidSession(session);
            }

            if(!StringUtils.isNumeric(userId)) {
                clearSession(session);
                logger.warn("Session validation failed due to non-numeric userId");
                return errorUtils.invalidSession(session);
            }

        }

        return ResponseEntity.ok().body(new MessageDTO(session, "Session is valid."));
    }

    /****

        Operation: ValidateStatus

        Normalizes the result of a backend session status check into a {@link MessageDTO}-based HTTP response by evaluating
        the provided {@link ResponseEntity} and its {@link StatusDTO} payload. Clears session attributes on invalid or
        non-successful outcomes to prevent reuse of stale authentication data, and maps specific {@link SessionStatus}
        values (e.g., NOT_FOUND, EXPIRED) to corresponding error responses. Returns an OK response when the backend confirms
        the session as valid.

        <p>

            Uses:

            <ul>
                <li>{@link ErrorUtils} for building {@link MessageDTO}-based error responses</li>
                <li>{@link StatusDTO} for retrieving the backend {@link SessionStatus}</li>
                <li>{@link SessionStatus} for interpreting session validity states</li>
                <li>{@link CookieDTO} for clearing invalid session attributes</li>
                <li>{@code clearSession(...)} for removing authentication attributes on failure</li>
                <li>{@link Logger} for audit and diagnostic logging</li>
            </ul>

        </p>

        @param response the backend response containing a {@link StatusDTO} with the evaluated {@link SessionStatus}

        @param session the cookie/session carrier to be cleared when the session is invalid, expired, or not found

        @param userType the expected user type context for the validation flow (used to scope the check in the calling layer)

        @return a response entity containing a {@link MessageDTO} when the session is valid, or an error response when the
        backend indicates the session is missing, expired, or otherwise invalid
    */
    private ResponseEntity<MessageDTO> validateSessionStatus(ResponseEntity<StatusDTO> response, CookieDTO session, UserType userType) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.MESSAGE_DTO);
    
        // Validate response body
        if(response.getBody() == null) {
            clearSession(session);
            logger.warn("Session validation failed due to missing response body");
            return errorUtils.sessionNotFound(session);
        }

        // Get session status
        SessionStatus status = response.getBody().getStatus();

        // Handle non-successful responses
        if(!response.getStatusCode().is2xxSuccessful()) {
            clearSession(session);

            if(status == SessionStatus.NOT_FOUND) {
                logger.warn("Session validation failed: session not found");
                return errorUtils.sessionNotFound(session);
            }
                
            if(status == SessionStatus.EXPIRED) {
                logger.warn("Session validation failed: session expired");
                return errorUtils.sessionExpired(session);
            }

            if(status != SessionStatus.VALID) {
                clearSession(session);
                logger.warn("Session validation failed: session not found");
                return errorUtils.sessionNotFound(session);
            }
        }

        return ResponseEntity.ok().body(new MessageDTO(session, "Session is valid."));
    }

    ///HELPER METHODS END

}   

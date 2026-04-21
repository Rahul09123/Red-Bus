package com.shubilet.security_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shubilet.security_service.controllers.Impl.AuthControllerImpl;
import com.shubilet.security_service.dataTransferObjects.CookieDTO;
import com.shubilet.security_service.dataTransferObjects.requests.LoginDTO;
import com.shubilet.security_service.dataTransferObjects.responses.CheckMessageDTO;
import com.shubilet.security_service.dataTransferObjects.responses.MessageDTO;


/****

    Domain: Authentication

    Declares the REST API contract for authentication session lifecycle operations under the {@code /api/auth} resource.
    This interface defines endpoints to create a session, terminate a session, and validate session state for generic users
    as well as role-scoped checks for ADMIN, COMPANY, and CUSTOMER contexts. Implementations are expected to perform
    request validation, enforce user-type constraints where applicable, and return standardized DTO-based responses that
    include the current {@link CookieDTO} session context and business messages describing the outcome.

    <p>

        Technologies:

        <ul>
            <li>Spring Web</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @see AuthControllerImpl

    @version 2.0
*/
@RestController
@RequestMapping("/api/auth")
public interface AuthController {

    /****

        Operation: CreateSession

        Defines the contract for initiating an authenticated user session by accepting identity and type information
        encapsulated in {@link LoginDTO}. Implementations are responsible for validating the request, preventing duplicate
        active sessions, delegating session creation based on user type, and returning a {@link MessageDTO} that contains
        the resulting {@link CookieDTO} session context along with a business message.

        <p>

            Usage:
            <pre>
                POST /api/auth/createSession

                Request Body:
                {
                    "cookie": {
                        "userId": null,
                        "userType": null,
                        "authCode": null
                    },
                    "userId": 123,
                    "userType": "ADMIN"
                }

                Response:
                {
                    "cookie": {
                        "userId": "123",
                        "userType": "ADMIN",
                        "authCode": "generated-auth-code"
                    },
                    "message": "Login successful."
                }

            </pre>
        </p>

        <p>

            Uses:

            <ul>
                <li>{@link LoginDTO} as the request payload carrying userId, userType, and cookie/session context</li>
                <li>{@link CookieDTO} as the cookie/session carrier embedded within the request and response</li>
                <li>{@link MessageDTO} as the standardized response wrapper conveying business messages</li>
                <li>{@link ResponseEntity} for HTTP-level response abstraction</li>
            </ul>

        </p>

        @param loginDTO the login request containing userId, userType, and cookie/session context

        @return a response entity containing a {@link MessageDTO} with the updated {@link CookieDTO} and a business message
    */
    @PostMapping("/createSession")
    public ResponseEntity<MessageDTO> createSession (LoginDTO loginDTO);

    /****

        Operation: Logout

        Defines the contract for terminating an authenticated user session using the session context provided via
        {@link CookieDTO}. Implementations are responsible for validating the session attributes (userId, userType, authCode),
        invalidating the backend session state for the resolved user domain, and returning a {@link MessageDTO} that contains
        the cleared {@link CookieDTO} along with a business message describing the outcome.

        <p>

            Usage:

            <pre>

                POST /api/auth/logout

                Request Body:
                {
                    "userId": "123",
                    "userType": "ADMIN",
                    "authCode": "existing-auth-code"
                }

                Response:
                {
                    "cookie": {
                        "userId": null,
                        "userType": null,
                        "authCode": null
                    },
                    "message": "Logout successful."
                }

            </pre>
        </p>

        <p>

            Uses:

            <ul>
                <li>{@link CookieDTO} as the session/cookie carrier for userId, userType, and authCode</li>
                <li>{@link MessageDTO} as the standardized response wrapper conveying business messages</li>
                <li>{@link ResponseEntity} for HTTP-level response abstraction</li>
            </ul>

        </p>

        @param session the session payload containing userId, userType, and authCode to be invalidated

        @return a response entity containing a {@link MessageDTO} with the cleared {@link CookieDTO} and a business message
    */
    @PostMapping("/logout")
    public ResponseEntity<MessageDTO> logout (CookieDTO session);

    /****

        Operation: Check

        Defines the contract for validating whether the provided {@link CookieDTO} represents a valid and active
        authenticated user session. Implementations are responsible for verifying the presence and correctness of
        required session attributes (userId, userType, authCode), delegating the validation to the appropriate
        backend session service based on user type, and returning a {@link MessageDTO} that reflects the session state.

        <p>

            Usage:

            <pre>

                POST /api/auth/check

                Request Body:
                {
                    "userId": "123",
                    "userType": "CUSTOMER",
                    "authCode": "existing-auth-code"
                }

                Response:
                {
                    "cookie": {
                        "userId": "123",
                        "userType": "CUSTOMER",
                        "authCode": "existing-auth-code"
                    },
                    "message": "Session is valid."
                }

            </pre>
        </p>

        <p>

            Uses:

            <ul>
                <li>{@link CookieDTO} as the session/cookie carrier for userId, userType, and authCode</li>
                <li>{@link MessageDTO} as the standardized response wrapper conveying session status</li>
                <li>{@link ResponseEntity} for HTTP-level response abstraction</li>
            </ul>

        </p>

        @param session the session payload containing userId, userType, and authCode to be validated

        @return a response entity containing a {@link MessageDTO} indicating whether the session is valid or not
    */
    @PostMapping("/check")
    public ResponseEntity<MessageDTO> check (CookieDTO session);

    /****

        Operation: CheckAdmin

        Defines the contract for validating whether the provided {@link CookieDTO} represents a valid and active
        authenticated session belonging specifically to an ADMIN user. Implementations are responsible for verifying
        the session attributes, enforcing ADMIN-only access, delegating the validation to the admin session service,
        and returning a {@link CheckMessageDTO} that includes the resolved admin userId upon successful validation.

        <p>

            Usage:

            <pre>

                POST /api/auth/checkAdmin

                Request Body:
                {
                    "userId": "1",
                    "userType": "ADMIN",
                    "authCode": "existing-auth-code"
                }

                Response:
                {
                    "cookie": {
                        "userId": "1",
                        "userType": "ADMIN",
                        "authCode": "existing-auth-code"
                    },
                    "message": "Session is valid.",
                    "userId": 1
                }

            </pre>
        </p>

        <p>

            Uses:

            <ul>
                <li>{@link CookieDTO} as the session/cookie carrier for userId, userType, and authCode</li>
                <li>{@link CheckMessageDTO} as the response wrapper including session context and resolved admin userId</li>
                <li>{@link ResponseEntity} for HTTP-level response abstraction</li>
            </ul>

        </p>

        @param session the session payload containing authentication attributes to be validated for ADMIN scope

        @return a response entity containing a {@link CheckMessageDTO} with session information and admin userId
    */
    @PostMapping("/checkAdmin")
    public ResponseEntity<CheckMessageDTO> checkAdminSession (CookieDTO session);
    
    /****

        Operation: CheckCompany

        Defines the contract for validating whether the provided {@link CookieDTO} represents a valid and active
        authenticated session belonging specifically to a COMPANY user. Implementations are responsible for verifying
        the session attributes, enforcing COMPANY-only access, delegating the validation to the company session service,
        and returning a {@link CheckMessageDTO} that includes the resolved company userId upon successful validation.

        <p>

            Usage:

            <pre>

                POST /api/auth/checkCompany

                Request Body:
                {
                    "userId": "10",
                    "userType": "COMPANY",
                    "authCode": "existing-auth-code"
                }

                Response:
                {
                    "cookie": {
                        "userId": "10",
                        "userType": "COMPANY",
                        "authCode": "existing-auth-code"
                    },
                    "message": "Session is valid.",
                    "userId": 10
                }

            </pre>
        </p>

        <p>

            Uses:

            <ul>
                <li>{@link CookieDTO} as the session/cookie carrier for userId, userType, and authCode</li>
                <li>{@link CheckMessageDTO} as the response wrapper including session context and resolved company userId</li>
                <li>{@link ResponseEntity} for HTTP-level response abstraction</li>
            </ul>

        </p>

        @param session the session payload containing authentication attributes to be validated for COMPANY scope

        @return a response entity containing a {@link CheckMessageDTO} with session information and company userId
    */
    @PostMapping("/checkCompany")
    public ResponseEntity<CheckMessageDTO> checkCompanySession (CookieDTO session);

    /****

        Operation: CheckCustomer

        Defines the contract for validating whether the provided {@link CookieDTO} represents a valid and active
        authenticated session belonging specifically to a CUSTOMER user. Implementations are responsible for verifying
        the session attributes, enforcing CUSTOMER-only access, delegating the validation to the customer session service,
        and returning a {@link CheckMessageDTO} that includes the resolved customer userId upon successful validation.

        <p>

            Usage:

            <pre>

                POST /api/auth/checkCustomer

                Request Body:
                {
                    "userId": "25",
                    "userType": "CUSTOMER",
                    "authCode": "existing-auth-code"
                }

                Response:
                {
                    "cookie": {
                        "userId": "25",
                        "userType": "CUSTOMER",
                        "authCode": "existing-auth-code"
                    },
                    "message": "Session is valid.",
                    "userId": 25
                }

            </pre>
        </p>

        <p>

            Uses:

            <ul>
                <li>{@link CookieDTO} as the session/cookie carrier for userId, userType, and authCode</li>
                <li>{@link CheckMessageDTO} as the response wrapper including session context and resolved customer userId</li>
                <li>{@link ResponseEntity} for HTTP-level response abstraction</li>
            </ul>

        </p>

        @param session the session payload containing authentication attributes to be validated for CUSTOMER scope

        @return a response entity containing a {@link CheckMessageDTO} with session information and customer userId
    */
    @PostMapping("/checkCustomer")
    public ResponseEntity<CheckMessageDTO> checkCustomerSession (CookieDTO session);
}

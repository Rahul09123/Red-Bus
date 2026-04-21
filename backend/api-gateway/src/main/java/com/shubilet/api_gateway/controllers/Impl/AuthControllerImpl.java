package com.shubilet.api_gateway.controllers.Impl;

import com.shubilet.api_gateway.common.constants.ServiceURLs;
import com.shubilet.api_gateway.controllers.AuthController;
import com.shubilet.api_gateway.dataTransferObjects.MessageDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.requests.auth.AdminRegistrationDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.requests.auth.CompanyRegistrationDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.requests.auth.CustomerRegistrationDTO;


import com.shubilet.api_gateway.dataTransferObjects.external.requests.auth.MemberCredentialsDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.CookieDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.auth.SessionCreationDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.CookieInfoDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.auth.MemberCheckMessageDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.auth.MemberSessionInfoDTO;
import com.shubilet.api_gateway.managers.HttpSessionManager;
import com.shubilet.api_gateway.mappers.auth.MemberSessionMapper;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthControllerImpl implements AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthControllerImpl.class);

    private final RestTemplate restTemplate;
    private final HttpSessionManager httpSessionManager;
    private final MemberSessionMapper memberSessionMapper;


    public AuthControllerImpl(RestTemplate restTemplate, MemberSessionMapper memberSessionMapper) {
        this.restTemplate = restTemplate;
        this.memberSessionMapper = memberSessionMapper;
        this.httpSessionManager = new HttpSessionManager();
    }

    @PostMapping("/session/check")
    @Override
    public ResponseEntity<MessageDTO> checkSession(HttpSession httpSession) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Starting Customer Registration (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<CookieInfoDTO> securityServiceCheckSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_SESSION_URL,
                HttpMethod.POST,
                securityServiceCheckSessionRequest,
                CookieInfoDTO.class
        );
        cookieDTO = securityServiceCheckSessionResponse.getBody().getCookie();
        httpSessionManager.updateSessionCookie(httpSession, cookieDTO);

        // There is already an Existing Session
        if (securityServiceCheckSessionResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Session is valid (requestId={})", requestId);
        }
        // No User is Logged in Clarified by Security Service
        if (securityServiceCheckSessionResponse.getStatusCode().is4xxClientError()) {
            logger.info("No user is currently logged in verified (requestId={})", requestId);
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageDTO("No user is currently logged in."));
        }

        // Something Went Wrong on Security Service
        if (securityServiceCheckSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckSessionResponse.getBody().getMessage()));
        }

        // Operation Completed Successfully
        return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Session Existence Check Completed."));
    }
    @PostMapping("/register/customer")
    @Override
    public ResponseEntity<MessageDTO> registerCustomer(HttpSession httpSession, @RequestBody CustomerRegistrationDTO customerRegistrationDTO) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Starting Customer Registration (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<CookieInfoDTO> securityServiceCheckSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_SESSION_URL,
                HttpMethod.POST,
                securityServiceCheckSessionRequest,
                CookieInfoDTO.class
        );
        cookieDTO = securityServiceCheckSessionResponse.getBody().getCookie();
        httpSessionManager.updateSessionCookie(httpSession, cookieDTO);

        // There is already an Existing Session
        if (securityServiceCheckSessionResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity
                    .status(securityServiceCheckSessionResponse.getStatusCode())
                    .body(new MessageDTO("There is already an existing logged in session."));
        }
        // No User is Logged in Clarified by Security Service
        if (securityServiceCheckSessionResponse.getStatusCode().is4xxClientError()) {
            logger.info("No user is currently logged in verified (requestId={})", requestId);
        }

        // Something Went Wrong on Security Service
        if (securityServiceCheckSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckSessionResponse.getBody().getMessage()));
        }

        // Send Request to Member Service for Registering a New Customer
        HttpEntity<CustomerRegistrationDTO> memberServiceRequest = new HttpEntity<>(customerRegistrationDTO, headers);
        ResponseEntity<MessageDTO> memberServiceResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_CUSTOMER_REGISTRATION_URL,
                HttpMethod.POST,
                memberServiceRequest,
                MessageDTO.class
        );


        // New Customer has been Successfully Registered on Member Service
        if (memberServiceResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Registration succeeded (requestId={})", requestId);
        } else if (memberServiceResponse.getStatusCode().is4xxClientError()) {
            logger.info("Registration failed (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceResponse.getStatusCode())
                    .body(memberServiceResponse.getBody());
        }
        // Something Went Wrong on Member Service
        else if (memberServiceResponse.getStatusCode().is5xxServerError()) {
            logger.warn("Member service returned error (status={} requestId={})", memberServiceResponse.getStatusCode(), requestId);
            return ResponseEntity
                    .status(memberServiceResponse.getStatusCode())
                    .body(memberServiceResponse.getBody());
        }

        // Operation Completed Successfully
        return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Customer Registration Successful."));
    }

    @PostMapping("/register/company")
    @Override
    public ResponseEntity<MessageDTO> registerCompany(HttpSession httpSession, @RequestBody CompanyRegistrationDTO companyRegistrationDTO) {
        String requestId = UUID.randomUUID().toString();

        logger.info("Starting Company Registration (requestId={})", requestId);


        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<CookieInfoDTO> securityServiceCheckSessionResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_COMPANY_REGISTRATION_URL,
                HttpMethod.POST,
                securityServiceCheckSessionRequest,
                CookieInfoDTO.class
        );
        cookieDTO = securityServiceCheckSessionResponse.getBody().getCookie();
        httpSessionManager.updateSessionCookie(httpSession, cookieDTO);

        // There is already an Existing Session
        if (securityServiceCheckSessionResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO("There is already an existing logged in session."));
        }
        // No User is Logged in Clarified by Security Service
        if (securityServiceCheckSessionResponse.getStatusCode().is4xxClientError()) {
            logger.info("No user is currently logged in verified (requestId={})", requestId);
        }

        // Something Went Wrong on Security Service
        if (securityServiceCheckSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckSessionResponse.getBody().getMessage()));
        }

        // Send Request to Member Service for Registering a New Customer
        HttpEntity<CompanyRegistrationDTO> memberServiceRequest = new HttpEntity<>(companyRegistrationDTO, headers);
        ResponseEntity<MessageDTO> memberServiceResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_COMPANY_REGISTRATION_URL,
                HttpMethod.POST,
                memberServiceRequest,
                MessageDTO.class
        );


        // New Customer has been Successfully Registered on Member Service
        if (memberServiceResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Registration succeeded (requestId={})", requestId);
        }
        else if (memberServiceResponse.getStatusCode().is4xxClientError()) {
            logger.info("Registration failed (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceResponse.getStatusCode())
                    .body(memberServiceResponse.getBody());
        }
        // Something Went Wrong on Member Service
        else if (memberServiceResponse.getStatusCode().is5xxServerError()) {
            logger.warn("Member service returned error (status={} requestId={})", memberServiceResponse.getStatusCode(), requestId);
            return ResponseEntity
                    .status(memberServiceResponse.getStatusCode())
                    .body(memberServiceResponse.getBody());
        }

        // Operation Completed Successfully
        return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Company Registration Successful."));
    }

    @PostMapping("/register/admin")
    @Override
    public ResponseEntity<MessageDTO> registerAdmin(HttpSession httpSession, @RequestBody AdminRegistrationDTO adminRegistrationDTO) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Starting Admin Registration (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<CookieInfoDTO> securityServiceCheckSessionResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_COMPANY_REGISTRATION_URL,
                HttpMethod.POST,
                securityServiceCheckSessionRequest,
                CookieInfoDTO.class
        );

        // There is already an Existing Session
        if (securityServiceCheckSessionResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO("There is already an existing logged in session."));
        }
        // No User is Logged in Clarified by Security Service
        if (securityServiceCheckSessionResponse.getStatusCode().is4xxClientError()) {
            logger.info("No user is currently logged in verified (requestId={})", requestId);
        }

        // Something Went Wrong on Security Service
        if (securityServiceCheckSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckSessionResponse.getBody().getMessage()));
        }

        cookieDTO = securityServiceCheckSessionResponse.getBody().getCookie();
        httpSessionManager.updateSessionCookie(httpSession, cookieDTO);

        // Send Request to Member Service for Registering a New Customer
        HttpEntity<AdminRegistrationDTO> memberServiceRequest = new HttpEntity<>(adminRegistrationDTO, headers);
        ResponseEntity<MessageDTO> memberServiceResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_ADMIN_REGISTRATION_URL,
                HttpMethod.POST,
                memberServiceRequest,
                MessageDTO.class
        );

        // New Customer has been Successfully Registered on Member Service
        if (memberServiceResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Registration succeeded (requestId={})", requestId);
        }
        else if (memberServiceResponse.getStatusCode().is4xxClientError()) {
            logger.info("Registration failed (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceResponse.getStatusCode())
                    .body(memberServiceResponse.getBody());
        }
        // Something Went Wrong on Member Service
        else if (memberServiceResponse.getStatusCode().is5xxServerError()) {
            logger.warn("Member service returned error (status={} requestId={})", memberServiceResponse.getStatusCode(), requestId);
            return ResponseEntity.status(memberServiceResponse.getStatusCode()).body(memberServiceResponse.getBody());
        }

        // Operation Completed Successfully
        return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Admin Registration Successful."));
    }

    @PostMapping("/session/check/admin")
    @Override
    public ResponseEntity<MessageDTO> checkAdminSession(HttpSession httpSession) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Admin Session Check (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<MemberCheckMessageDTO> securityServiceCheckSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_ADMIN_SESSION_URL,
                HttpMethod.POST,
                securityServiceCheckSessionRequest,
                MemberCheckMessageDTO.class
        );

        cookieDTO = securityServiceCheckSessionResponse.getBody().getCookie();
        httpSessionManager.updateSessionCookie(httpSession, cookieDTO);

        // Session is Valid and Admin is Logged in
        if (securityServiceCheckSessionResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Admin session is valid (requestId={})", requestId);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Admin session is valid."));
        }
        // No Valid Session Found
        else if (securityServiceCheckSessionResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity
                    .status(securityServiceCheckSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckSessionResponse.getBody().getMessage()));
        }
        // Something Went Wrong on Security Service
        else if (securityServiceCheckSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckSessionResponse.getBody().getMessage()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageDTO("Unknown error occurred while checking admin session."));

    }

    @PostMapping("/session/check/company")
    @Override
    public ResponseEntity<MessageDTO> checkCompanySession(HttpSession httpSession) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Company Session Check (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<MemberCheckMessageDTO> securityServiceCheckSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_COMPANY_SESSION_URL,
                HttpMethod.POST,
                securityServiceCheckSessionRequest,
                MemberCheckMessageDTO.class
        );

        cookieDTO = securityServiceCheckSessionResponse.getBody().getCookie();
        httpSessionManager.updateSessionCookie(httpSession, cookieDTO);

        // Session is Valid and Company is Logged in
        if (securityServiceCheckSessionResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Company session is valid (requestId={})", requestId);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Company session is valid."));
        }
        // No Valid Session Found
        else if (securityServiceCheckSessionResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity
                    .status(securityServiceCheckSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckSessionResponse.getBody().getMessage()));
        }
        // Something Went Wrong on Security Service
        else if (securityServiceCheckSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckSessionResponse.getBody().getMessage()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageDTO("Unknown error occurred while checking company session."));
    }

    @PostMapping("/session/check/customer")
    @Override
    public ResponseEntity<MessageDTO> checkCustomerSession(HttpSession httpSession) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Customer Session Check (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<MemberCheckMessageDTO> securityServiceCheckSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_CUSTOMER_SESSION_URL,
                HttpMethod.POST,
                securityServiceCheckSessionRequest,
                MemberCheckMessageDTO.class
        );

        cookieDTO = securityServiceCheckSessionResponse.getBody().getCookie();
        httpSessionManager.updateSessionCookie(httpSession, cookieDTO);

        // Session is Valid and Customer is Logged in
        if (securityServiceCheckSessionResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Customer session is valid (requestId={})", requestId);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Customer session is valid."));
        }
        // No Valid Session Found
        else if (securityServiceCheckSessionResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity
                    .status(securityServiceCheckSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckSessionResponse.getBody().getMessage()));
        }
        // Something Went Wrong on Security Service
        else if (securityServiceCheckSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckSessionResponse.getBody().getMessage()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageDTO("Unknown error occurred while checking customer session."));
    }

    @PostMapping("/login")
    @Override
    public ResponseEntity<MessageDTO> login(HttpSession httpSession, @RequestBody MemberCredentialsDTO memberCredentialsDTO) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Login (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<CookieInfoDTO> securityServiceCheckSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_SESSION_URL,
                HttpMethod.POST,
                securityServiceCheckSessionRequest,
                CookieInfoDTO.class
        );

        cookieDTO = securityServiceCheckSessionResponse.getBody().getCookie();
        httpSessionManager.updateSessionCookie(httpSession, cookieDTO);

        // There is already an Existing Session
        if (securityServiceCheckSessionResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MessageDTO("There is already an existing logged in session."));

        } else if (securityServiceCheckSessionResponse.getStatusCode().is4xxClientError()) {
            logger.info("No user is currently logged in verified (requestId={})", requestId);

        } else if (securityServiceCheckSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity.
                    status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body(new MessageDTO("Something went wrong while checking existing session."));
        }

        // Send Request to Member Service For Checking User Credentials
        HttpEntity<MemberCredentialsDTO> memberServiceCredentialCheckRequest = new HttpEntity<>(memberCredentialsDTO, headers);
        ResponseEntity<MemberSessionInfoDTO> memberServiceCredentialCheckResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_CREDENTIALS_CHECK_URL,
                HttpMethod.POST,
                memberServiceCredentialCheckRequest,
                MemberSessionInfoDTO.class
        );

        // User Credentials are Valid
        if (memberServiceCredentialCheckResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Member credentials are valid (requestId={})", requestId);
        }
        // User Credentials are Invalid
        else if (memberServiceCredentialCheckResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity
                    .status(memberServiceCredentialCheckResponse.getStatusCode())
                    .body(new MessageDTO(memberServiceCredentialCheckResponse.getBody().getMessage()));
        }
        // Something went wrong on Member Service
        else if (memberServiceCredentialCheckResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(memberServiceCredentialCheckResponse.getStatusCode())
                    .body(new MessageDTO(memberServiceCredentialCheckResponse.getBody().getMessage()));
        }

        // Convert Member Service Response to DTO For Next Request
        SessionCreationDTO sessionCreationDTO = memberSessionMapper.toSessionCreationDTO(cookieDTO, memberServiceCredentialCheckResponse.getBody());

        // Send Request to Security Service For Creating Session
        HttpEntity<SessionCreationDTO> securityServiceSessionCreationRequest = new HttpEntity<>(sessionCreationDTO, headers);
        ResponseEntity<CookieInfoDTO> securityServiceSessionCreationResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CREATE_SESSION_URL,
                HttpMethod.POST,
                securityServiceSessionCreationRequest,
                CookieInfoDTO.class
        );
        // Session Creation Successful on Security Service
        if (securityServiceSessionCreationResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Session Creation succeeded (requestId={})", requestId);
        }
        //
        else if (securityServiceSessionCreationResponse.getStatusCode().is4xxClientError()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // Something went wrong on Security Service
        else if (securityServiceSessionCreationResponse.getStatusCode().is5xxServerError()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        cookieDTO = securityServiceSessionCreationResponse.getBody().getCookie();
        httpSessionManager.updateSessionCookie(httpSession, cookieDTO);

        // Operation Completed Successfully
        return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Member Successfully Logged in."));
    }

    @PostMapping("/logout")
    @Override
    public ResponseEntity<MessageDTO> logout(HttpSession httpSession) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Expedition Search (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceDeleteSessionRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<MemberCheckMessageDTO> securityServiceCheckDeleteSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_DELETE_SESSION_URL,
                HttpMethod.POST,
                securityServiceDeleteSessionRequest,
                MemberCheckMessageDTO.class
        );

        cookieDTO = securityServiceCheckDeleteSessionResponse.getBody().getCookie();
        httpSessionManager.updateSessionCookie(httpSession, cookieDTO);

        // Session Clearing Clarified by Security Service
        if (securityServiceCheckDeleteSessionResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Session Delete Successful (requestId={})", requestId);
        }

        // Bad Request to Security Service
        if (securityServiceCheckDeleteSessionResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity
                    .status(securityServiceCheckDeleteSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckDeleteSessionResponse.getBody().getMessage()));
        }

        // Something Went Wrong on Security Service
        if (securityServiceCheckDeleteSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckDeleteSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckDeleteSessionResponse.getBody().getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Member Successfully Logged out."));
    }
}

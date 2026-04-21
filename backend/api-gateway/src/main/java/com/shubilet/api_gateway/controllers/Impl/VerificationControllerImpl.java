package com.shubilet.api_gateway.controllers.Impl;

import com.shubilet.api_gateway.common.constants.ServiceURLs;
import com.shubilet.api_gateway.controllers.VerificationController;
import com.shubilet.api_gateway.dataTransferObjects.external.responses.verification.UnverifiedAdminsDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.responses.verification.UnverifiedCompaniesDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.verification.CompanyVerificationInternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.MessageDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.requests.VerificationController.AdminVerificationExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.requests.VerificationController.CompanyVerificationExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.CookieDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.AdminIdDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.verification.AdminVerificationInternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.auth.MemberCheckMessageDTO;
import com.shubilet.api_gateway.managers.HttpSessionManager;
import com.shubilet.api_gateway.mappers.CookieMapper;
import com.shubilet.api_gateway.mappers.verification.AdminVerificationExternalMapper;
import com.shubilet.api_gateway.mappers.verification.CompanyVerificationExternalMapper;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RestController
@RequestMapping("/api/verification")
public class VerificationControllerImpl implements VerificationController {
    private final Logger logger = LoggerFactory.getLogger(VerificationControllerImpl.class);
    private final RestTemplate restTemplate;
    private final HttpSessionManager httpSessionManager;
    private final CookieMapper cookieMapper;
    private final CompanyVerificationExternalMapper companyVerificationExternalMapper;
    private final AdminVerificationExternalMapper adminVerificationExternalMapper;

    public VerificationControllerImpl(RestTemplate restTemplate, CookieMapper cookieMapper,
                                      CompanyVerificationExternalMapper companyVerificationExternalMapper,
                                      AdminVerificationExternalMapper adminVerificationExternalMapper) {
        this.restTemplate = restTemplate;
        this.cookieMapper = cookieMapper;
        this.companyVerificationExternalMapper = companyVerificationExternalMapper;
        this.adminVerificationExternalMapper = adminVerificationExternalMapper;
        this.httpSessionManager = new HttpSessionManager();
    }


    @PostMapping("/verify/company")
    public ResponseEntity<MessageDTO> verifyCompany(HttpSession httpSession, @RequestBody CompanyVerificationExternalDTO companyVerificationExternalDTO) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Company Verification (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionCustomerRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<MemberCheckMessageDTO> securityServiceCheckCustomerSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_ADMIN_SESSION_URL,
                HttpMethod.POST,
                securityServiceCheckSessionCustomerRequest,
                MemberCheckMessageDTO.class
        );

        // There is already an Existing Session
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Customer Session Exists (requestId={})", requestId);
        }

        // No User is Logged in Clarified by Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity
                    .status(securityServiceCheckCustomerSessionResponse.getStatusCode())
                    .body(new MessageDTO("There is no Existing Admin Session."));
        }

        // Something Went Wrong on Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckCustomerSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckCustomerSessionResponse.getBody().getMessage()));
        }
        CompanyVerificationInternalDTO companyVerificationInternalDTO = companyVerificationExternalMapper.toCompanyVerificationInternalDTO(
                companyVerificationExternalDTO,
                securityServiceCheckCustomerSessionResponse.getBody()
        );

        HttpEntity<CompanyVerificationInternalDTO> memberServiceCompanyVerificationRequest = new HttpEntity<>(companyVerificationInternalDTO, headers);
        ResponseEntity<MessageDTO> memberServiceCompanyVerificationResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_VERIFY_COMPANY_URL,
                HttpMethod.POST,
                memberServiceCompanyVerificationRequest,
                MessageDTO.class
        );
        if (memberServiceCompanyVerificationResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Company Verified Successfully (requestId={})", requestId);
        } else if (memberServiceCompanyVerificationResponse.getStatusCode().is4xxClientError()) {
            logger.warn("Bad Request to MemberService(requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceCompanyVerificationResponse.getStatusCode())
                    .body(memberServiceCompanyVerificationResponse.getBody());
        } else if (memberServiceCompanyVerificationResponse.getStatusCode().is5xxServerError()) {
            logger.warn("Internal Server Error at MemberService(requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceCompanyVerificationResponse.getStatusCode())
                    .body(memberServiceCompanyVerificationResponse.getBody());
        }

        return ResponseEntity.ok().body(memberServiceCompanyVerificationResponse.getBody());
    }

    @PostMapping("/verify/admin")
    public ResponseEntity<MessageDTO> verifyAdmin(HttpSession httpSession, @RequestBody AdminVerificationExternalDTO adminVerificationExternalDTO) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Admin Verification (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionAdminRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<MemberCheckMessageDTO> securityServiceCheckAdminSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_ADMIN_SESSION_URL,
                HttpMethod.POST,
                securityServiceCheckSessionAdminRequest,
                MemberCheckMessageDTO.class
        );

        // There is already an Existing Session
        if (securityServiceCheckAdminSessionResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Customer Session Exists (requestId={})", requestId);
        }

        // No User is Logged in Clarified by Security Service
        if (securityServiceCheckAdminSessionResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity
                    .status(securityServiceCheckAdminSessionResponse.getStatusCode())
                    .body(new MessageDTO("There is no Existing Admin Session."));
        }

        // Something Went Wrong on Security Service
        if (securityServiceCheckAdminSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckAdminSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckAdminSessionResponse.getBody().getMessage()));
        }

        AdminVerificationInternalDTO adminVerificationInternalDTO = adminVerificationExternalMapper.toAdminVerificationInternalDTO(
                adminVerificationExternalDTO,
                securityServiceCheckAdminSessionResponse.getBody()
        );

        HttpEntity<AdminVerificationInternalDTO> memberServiceAdminVerificationRequest = new HttpEntity<>(adminVerificationInternalDTO, headers);
        ResponseEntity<MessageDTO> memberServiceAdminVerificationResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_VERIFY_ADMIN_URL,
                HttpMethod.POST,
                memberServiceAdminVerificationRequest,
                MessageDTO.class
        );
        if (memberServiceAdminVerificationResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Admin Verified Successfully (requestId={})", requestId);
        } else if (memberServiceAdminVerificationResponse.getStatusCode().is4xxClientError()) {
            logger.warn("Bad Request to Member Service (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceAdminVerificationResponse.getStatusCode())
                    .body(memberServiceAdminVerificationResponse.getBody());
        } else if (memberServiceAdminVerificationResponse.getStatusCode().is5xxServerError()) {
            logger.warn("Internal Server Error at Member Service(requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceAdminVerificationResponse.getStatusCode())
                    .body(memberServiceAdminVerificationResponse.getBody());
        }
        return ResponseEntity.ok().body(memberServiceAdminVerificationResponse.getBody());
    }

    @PostMapping("/get/unverified/companies")
    public ResponseEntity<UnverifiedCompaniesDTO> sendUnverifiedCompanies(HttpSession httpSession) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Sending Unverified Companies (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionAdminRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<MemberCheckMessageDTO> securityServiceCheckAdminSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_ADMIN_SESSION_URL,
                HttpMethod.POST,
                securityServiceCheckSessionAdminRequest,
                MemberCheckMessageDTO.class
        );

        // There is already an Existing Session
        if (securityServiceCheckAdminSessionResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Admin Session Exists (requestId={})", requestId);
        }
        else if (securityServiceCheckAdminSessionResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity
                    .status(securityServiceCheckAdminSessionResponse.getStatusCode())
                    .body(new UnverifiedCompaniesDTO("There is no Existing Admin Session."));
        } else if (securityServiceCheckAdminSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckAdminSessionResponse.getStatusCode())
                    .body(new UnverifiedCompaniesDTO(securityServiceCheckAdminSessionResponse.getBody().getMessage()));
        }

        AdminIdDTO adminIdDTO = cookieMapper.toAdminIdDTO(cookieDTO);
        logger.info("AdminIdDTO: {}", adminIdDTO.getAdminId());
        HttpEntity<AdminIdDTO> memberServiceUnverifiedCompaniesRequest = new HttpEntity<>(adminIdDTO, headers);
        ResponseEntity<UnverifiedCompaniesDTO> memberServiceUnverifiedCompaniesResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_GET_UNVERIFIED_COMPANIES_URL,
                HttpMethod.POST,
                memberServiceUnverifiedCompaniesRequest,
                UnverifiedCompaniesDTO.class
        );

        if (memberServiceUnverifiedCompaniesResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Unverified Companies Sent Successfully (requestId={})", requestId);
        } else if (memberServiceUnverifiedCompaniesResponse.getStatusCode().is4xxClientError()) {
            logger.warn("Bad Request to Member Service (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceUnverifiedCompaniesResponse.getStatusCode())
                    .body(memberServiceUnverifiedCompaniesResponse.getBody());
        } else if (memberServiceUnverifiedCompaniesResponse.getStatusCode().is5xxServerError()) {
            logger.warn("Internal Server Error at Member Service(requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceUnverifiedCompaniesResponse.getStatusCode())
                    .body(memberServiceUnverifiedCompaniesResponse.getBody());
        }

        return ResponseEntity.ok().body(memberServiceUnverifiedCompaniesResponse.getBody());
    }
    
    @PostMapping("/get/unverified/admins")
    public ResponseEntity<UnverifiedAdminsDTO> sendUnverifiedAdmins(HttpSession httpSession) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Sending Unverified Admins (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionAdminRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<MemberCheckMessageDTO> securityServiceCheckAdminSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_ADMIN_SESSION_URL,
                HttpMethod.POST,
                securityServiceCheckSessionAdminRequest,
                MemberCheckMessageDTO.class
        );

        // There is already an Existing Session
        if (securityServiceCheckAdminSessionResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Admin Session Exists (requestId={})", requestId);
        } else if (securityServiceCheckAdminSessionResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity
                    .status(securityServiceCheckAdminSessionResponse.getStatusCode())
                    .body(new UnverifiedAdminsDTO("There is no Existing Admin Session."));
        } else if (securityServiceCheckAdminSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckAdminSessionResponse.getStatusCode())
                    .body(new UnverifiedAdminsDTO(securityServiceCheckAdminSessionResponse.getBody().getMessage()));
        }

        AdminIdDTO adminIdDTO = cookieMapper.toAdminIdDTO(cookieDTO);
        logger.info("AdminIdDTO: {}", adminIdDTO.getAdminId());
        HttpEntity<AdminIdDTO> memberServiceUnverifiedAdminsRequest = new HttpEntity<>(adminIdDTO, headers);
        ResponseEntity<UnverifiedAdminsDTO> memberServiceUnverifiedAdminsResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_GET_UNVERIFIED_ADMINS_URL,
                HttpMethod.POST,
                memberServiceUnverifiedAdminsRequest,
                UnverifiedAdminsDTO.class
        );

        if (memberServiceUnverifiedAdminsResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Unverified Admins Sent Successfully (requestId={})", requestId);
        } else if (memberServiceUnverifiedAdminsResponse.getStatusCode().is4xxClientError()) {
            logger.warn("Bad Request to Member Service (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceUnverifiedAdminsResponse.getStatusCode())
                    .body(memberServiceUnverifiedAdminsResponse.getBody());
        } else if (memberServiceUnverifiedAdminsResponse.getStatusCode().is5xxServerError()) {
            logger.warn("Internal Server Error at Member Service(requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceUnverifiedAdminsResponse.getStatusCode())
                    .body(memberServiceUnverifiedAdminsResponse.getBody());
        }

        return ResponseEntity.ok().body(memberServiceUnverifiedAdminsResponse.getBody());
    }
}

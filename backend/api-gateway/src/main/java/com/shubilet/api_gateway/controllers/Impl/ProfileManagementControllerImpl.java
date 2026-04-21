package com.shubilet.api_gateway.controllers.Impl;

import com.shubilet.api_gateway.common.constants.ServiceURLs;
import com.shubilet.api_gateway.controllers.ProfileManagementController;
import com.shubilet.api_gateway.dataTransferObjects.MessageDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.requests.profileManagement.CardCreationExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.requests.profileManagement.CardDeletionExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.requests.profileManagement.FavoriteCompanyAdditionExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.requests.profileManagement.FavoriteCompanyDeletionExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.requests.profileManagement.MemberAttributeChangeExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.responses.profileManagement.AdminProfileDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.responses.profileManagement.CardsDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.responses.profileManagement.CompanyProfileDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.responses.profileManagement.CustomerProfileDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.AdminIdDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.CompanyIdDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.CustomerIdDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.profileManagement.CardCreationInternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.profileManagement.CardDeletionInternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.profileManagement.FavoriteCompanyAdditionInternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.profileManagement.FavoriteCompanyDeletionInternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.profileManagement.MemberAttributeChangeInternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.CookieDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.auth.MemberCheckMessageDTO;
import com.shubilet.api_gateway.managers.HttpSessionManager;
import com.shubilet.api_gateway.mappers.CookieMapper;
import com.shubilet.api_gateway.mappers.profileManagement.CardCreationExternalMapper;
import com.shubilet.api_gateway.mappers.profileManagement.CardDeletionExternalMapper;
import com.shubilet.api_gateway.mappers.profileManagement.FavoriteCompanyAdditionExternalMapper;
import com.shubilet.api_gateway.mappers.profileManagement.FavoriteCompanyDeletionExternalMapper;
import com.shubilet.api_gateway.mappers.profileManagement.MemberAttributeChangeExternalMapper;
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
@RequestMapping("/api/profile")
public class ProfileManagementControllerImpl implements ProfileManagementController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileManagementControllerImpl.class);
    private final RestTemplate restTemplate;
    private final HttpSessionManager httpSessionManager;
    private final CookieMapper cookieMapper;
    private final MemberAttributeChangeExternalMapper memberAttributeChangeExternalMapper;
    private final FavoriteCompanyAdditionExternalMapper favoriteCompanyAdditionExternalMapper;
    private final FavoriteCompanyDeletionExternalMapper favoriteCompanyDeletionExternalMapper;
    private final CardCreationExternalMapper cardCreationExternalMapper;
    private final CardDeletionExternalMapper cardDeletionExternalMapper;


    public ProfileManagementControllerImpl(
            RestTemplate restTemplate, CookieMapper cookieMapper, MemberAttributeChangeExternalMapper memberAttributeChangeExternalMapper,
            FavoriteCompanyAdditionExternalMapper favoriteCompanyAdditionExternalMapper,
            FavoriteCompanyDeletionExternalMapper favoriteCompanyDeletionExternalMapper,
            CardCreationExternalMapper cardCreationExternalMapper, CardDeletionExternalMapper cardDeletionExternalMapper
    ) {
        this.restTemplate = restTemplate;
        this.cookieMapper = cookieMapper;
        this.memberAttributeChangeExternalMapper = memberAttributeChangeExternalMapper;
        this.favoriteCompanyAdditionExternalMapper = favoriteCompanyAdditionExternalMapper;
        this.favoriteCompanyDeletionExternalMapper = favoriteCompanyDeletionExternalMapper;
        this.cardCreationExternalMapper = cardCreationExternalMapper;
        this.cardDeletionExternalMapper = cardDeletionExternalMapper;
        this.httpSessionManager = new HttpSessionManager();
    }

    @PostMapping("/customer/edit/name")
    @Override
    public ResponseEntity<MessageDTO> customerEditName(HttpSession httpSession, @RequestBody MemberAttributeChangeExternalDTO memberAttributeChangeExternalDTO) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Login (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionCustomerRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<MemberCheckMessageDTO> securityServiceCheckCustomerSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_CUSTOMER_SESSION_URL,
                HttpMethod.POST,
                securityServiceCheckSessionCustomerRequest,
                MemberCheckMessageDTO.class
        );

        cookieDTO = securityServiceCheckCustomerSessionResponse.getBody().getCookie();
        httpSessionManager.updateSessionCookie(httpSession, cookieDTO);

        // There is already an Existing Session
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Customer Session Exists (requestId={})", requestId);
        }

        // No User is Logged in Clarified by Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity
                    .status(securityServiceCheckCustomerSessionResponse.getStatusCode())
                    .body(new MessageDTO("There is no Existing Customer Session."));
        }

        // Something Went Wrong on Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckCustomerSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckCustomerSessionResponse.getBody().getMessage()));
        }

        MemberCheckMessageDTO memberCheckMessageDTO = securityServiceCheckCustomerSessionResponse.getBody();
        MemberAttributeChangeInternalDTO memberAttributeChangeInternalDTO = memberAttributeChangeExternalMapper.toMemberAttributeChangeInternalDTO(memberAttributeChangeExternalDTO, memberCheckMessageDTO);

        HttpEntity<MemberAttributeChangeInternalDTO> memberServiceCustomerProfileEditNameRequest = new HttpEntity<>(memberAttributeChangeInternalDTO, headers);
        ResponseEntity<MessageDTO> memberServiceCustomerProfileEditNameResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_CUSTOMER_PROFILE_EDIT_NAME_URL,
                HttpMethod.POST,
                memberServiceCustomerProfileEditNameRequest,
                MessageDTO.class
        );

        // Customer has been Successfully Updated on Member Service
        if (memberServiceCustomerProfileEditNameResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Customer Profile Name Update Succeeded (requestId={})", requestId);
        } else if (memberServiceCustomerProfileEditNameResponse.getStatusCode().is4xxClientError()) {
            logger.info("Customer Profile Name Update Failed (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceCustomerProfileEditNameResponse.getStatusCode())
                    .body(memberServiceCustomerProfileEditNameResponse.getBody());
        }
        // Something Went Wrong on Member Service
        else if (memberServiceCustomerProfileEditNameResponse.getStatusCode().is5xxServerError()) {
            logger.warn("Member service returned error (status={} requestId={})", memberServiceCustomerProfileEditNameResponse.getStatusCode(), requestId);
            return ResponseEntity
                    .status(memberServiceCustomerProfileEditNameResponse.getStatusCode())
                    .body(memberServiceCustomerProfileEditNameResponse.getBody());
        }

        return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Customer Name Successfully Updated"));
    }

    @PostMapping("/customer/edit/surname")
    @Override
    public ResponseEntity<MessageDTO> customerEditSurname(HttpSession httpSession, @RequestBody MemberAttributeChangeExternalDTO memberAttributeChangeExternalDTO) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Login (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionCustomerRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<MemberCheckMessageDTO> securityServiceCheckCustomerSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_CUSTOMER_SESSION_URL,
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
                    .body(new MessageDTO("There is no Existing Customer Session."));
        }

        // Something Went Wrong on Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckCustomerSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckCustomerSessionResponse.getBody().getMessage()));
        }

        MemberCheckMessageDTO memberCheckMessageDTO = securityServiceCheckCustomerSessionResponse.getBody();
        MemberAttributeChangeInternalDTO memberAttributeChangeInternalDTO = memberAttributeChangeExternalMapper.toMemberAttributeChangeInternalDTO(memberAttributeChangeExternalDTO, memberCheckMessageDTO);

        HttpEntity<MemberAttributeChangeInternalDTO> memberServiceCustomerProfileEditNameRequest = new HttpEntity<>(memberAttributeChangeInternalDTO, headers);
        ResponseEntity<MessageDTO> memberServiceCustomerProfileEditNameResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_CUSTOMER_PROFILE_EDIT_SURNAME_URL,
                HttpMethod.POST,
                memberServiceCustomerProfileEditNameRequest,
                MessageDTO.class
        );

        // Customer has been Successfully Updated on Member Service
        if (memberServiceCustomerProfileEditNameResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Customer Profile Surname Update Succeeded (requestId={})", requestId);
        } else if (memberServiceCustomerProfileEditNameResponse.getStatusCode().is4xxClientError()) {
            logger.info("Customer Profile Surname Update Failed (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceCustomerProfileEditNameResponse.getStatusCode())
                    .body(memberServiceCustomerProfileEditNameResponse.getBody());
        }
        // Something Went Wrong on Member Service
        else if (memberServiceCustomerProfileEditNameResponse.getStatusCode().is5xxServerError()) {
            logger.warn("Member service returned error (status={} requestId={})", memberServiceCustomerProfileEditNameResponse.getStatusCode(), requestId);
            return ResponseEntity.status(memberServiceCustomerProfileEditNameResponse.getStatusCode()).body(memberServiceCustomerProfileEditNameResponse.getBody());
        }
        return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Customer Surname Successfully Updated"));
    }

    @PostMapping("/customer/edit/gender")
    @Override
    public ResponseEntity<MessageDTO> customerEditGender(HttpSession httpSession, @RequestBody MemberAttributeChangeExternalDTO memberAttributeChangeExternalDTO) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Login (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionCustomerRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<MemberCheckMessageDTO> securityServiceCheckCustomerSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_CUSTOMER_SESSION_URL,
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
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MessageDTO("There is no Existing Customer Session."));
        }

        // Something Went Wrong on Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckCustomerSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckCustomerSessionResponse.getBody().getMessage()));
        }

        MemberCheckMessageDTO memberCheckMessageDTO = securityServiceCheckCustomerSessionResponse.getBody();
        MemberAttributeChangeInternalDTO memberAttributeChangeInternalDTO = memberAttributeChangeExternalMapper.toMemberAttributeChangeInternalDTO(memberAttributeChangeExternalDTO, memberCheckMessageDTO);

        HttpEntity<MemberAttributeChangeInternalDTO> memberServiceCustomerProfileEditNameRequest = new HttpEntity<>(memberAttributeChangeInternalDTO, headers);
        ResponseEntity<MessageDTO> memberServiceCustomerProfileEditNameResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_CUSTOMER_PROFILE_EDIT_GENDER_URL,
                HttpMethod.POST,
                memberServiceCustomerProfileEditNameRequest,
                MessageDTO.class
        );

        // Customer has been Successfully Updated on Member Service
        if (memberServiceCustomerProfileEditNameResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Customer Profile Gender Update Succeeded (requestId={})", requestId);
        } else if (memberServiceCustomerProfileEditNameResponse.getStatusCode().is4xxClientError()) {
            logger.info("Customer Profile Gender Update Failed (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceCustomerProfileEditNameResponse.getStatusCode())
                    .body(memberServiceCustomerProfileEditNameResponse.getBody());
        }
        // Something Went Wrong on Member Service
        else if (memberServiceCustomerProfileEditNameResponse.getStatusCode().is5xxServerError()) {
            logger.warn("Member service returned error (status={} requestId={})", memberServiceCustomerProfileEditNameResponse.getStatusCode(), requestId);
            return ResponseEntity
                    .status(memberServiceCustomerProfileEditNameResponse.getStatusCode())
                    .body(memberServiceCustomerProfileEditNameResponse.getBody());
        }
        return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Customer Gender Successfully Updated"));
    }

    @PostMapping("/customer/edit/email")
    @Override
    public ResponseEntity<MessageDTO> customerEditEmail(HttpSession httpSession, @RequestBody MemberAttributeChangeExternalDTO memberAttributeChangeExternalDTO) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Login (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionCustomerRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<MemberCheckMessageDTO> securityServiceCheckCustomerSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_CUSTOMER_SESSION_URL,
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
                    .body(new MessageDTO("There is no Existing Customer Session."));
        }

        // Something Went Wrong on Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckCustomerSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckCustomerSessionResponse.getBody().getMessage()));
        }

        MemberCheckMessageDTO memberCheckMessageDTO = securityServiceCheckCustomerSessionResponse.getBody();
        MemberAttributeChangeInternalDTO memberAttributeChangeInternalDTO = memberAttributeChangeExternalMapper.toMemberAttributeChangeInternalDTO(memberAttributeChangeExternalDTO, memberCheckMessageDTO);

        HttpEntity<MemberAttributeChangeInternalDTO> memberServiceCustomerProfileEditNameRequest = new HttpEntity<>(memberAttributeChangeInternalDTO, headers);
        ResponseEntity<MessageDTO> memberServiceCustomerProfileEditNameResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_CUSTOMER_PROFILE_EDIT_EMAIL_URL,
                HttpMethod.POST,
                memberServiceCustomerProfileEditNameRequest,
                MessageDTO.class
        );

        // Customer has been Successfully Updated on Member Service
        if (memberServiceCustomerProfileEditNameResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Customer Profile Email Update Succeeded (requestId={})", requestId);
        } else if (memberServiceCustomerProfileEditNameResponse.getStatusCode().is4xxClientError()) {
            logger.info("Customer Profile Email Update Failed (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceCustomerProfileEditNameResponse.getStatusCode())
                    .body(memberServiceCustomerProfileEditNameResponse.getBody());
        }
        // Something Went Wrong on Member Service
        else if (memberServiceCustomerProfileEditNameResponse.getStatusCode().is5xxServerError()) {
            logger.warn("Member service returned error (status={} requestId={})", memberServiceCustomerProfileEditNameResponse.getStatusCode(), requestId);
            return ResponseEntity
                    .status(memberServiceCustomerProfileEditNameResponse.getStatusCode())
                    .body(memberServiceCustomerProfileEditNameResponse.getBody());
        }
        return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Customer Email Successfully Updated"));
    }

    @PostMapping("/customer/edit/password")
    @Override
    public ResponseEntity<MessageDTO> customerEditPassword(HttpSession httpSession, @RequestBody MemberAttributeChangeExternalDTO memberAttributeChangeExternalDTO) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Login (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionCustomerRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<MemberCheckMessageDTO> securityServiceCheckCustomerSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_CUSTOMER_SESSION_URL,
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO("There is no Existing Customer Session."));
        }

        // Something Went Wrong on Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckCustomerSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckCustomerSessionResponse.getBody().getMessage()));
        }

        MemberCheckMessageDTO memberCheckMessageDTO = securityServiceCheckCustomerSessionResponse.getBody();
        MemberAttributeChangeInternalDTO memberAttributeChangeInternalDTO = memberAttributeChangeExternalMapper.toMemberAttributeChangeInternalDTO(memberAttributeChangeExternalDTO, memberCheckMessageDTO);

        HttpEntity<MemberAttributeChangeInternalDTO> memberServiceCustomerProfileEditNameRequest = new HttpEntity<>(memberAttributeChangeInternalDTO, headers);
        ResponseEntity<MessageDTO> memberServiceCustomerProfileEditNameResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_CUSTOMER_PROFILE_EDIT_PASSWORD_URL,
                HttpMethod.POST,
                memberServiceCustomerProfileEditNameRequest,
                MessageDTO.class
        );

        // Customer has been Successfully Updated on Member Service
        if (memberServiceCustomerProfileEditNameResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Customer Profile Password Update Succeeded (requestId={})", requestId);
        } else if (memberServiceCustomerProfileEditNameResponse.getStatusCode().is4xxClientError()) {
            logger.info("Customer Profile Password Update Failed (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceCustomerProfileEditNameResponse.getStatusCode())
                    .body(memberServiceCustomerProfileEditNameResponse.getBody());
        }
        // Something Went Wrong on Member Service
        else if (memberServiceCustomerProfileEditNameResponse.getStatusCode().is5xxServerError()) {
            logger.warn("Member service returned error (status={} requestId={})", memberServiceCustomerProfileEditNameResponse.getStatusCode(), requestId);
            return ResponseEntity
                    .status(memberServiceCustomerProfileEditNameResponse.getStatusCode())
                    .body(memberServiceCustomerProfileEditNameResponse.getBody());
        }
        return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Customer Password Successfully Updated"));
    }

    @PostMapping("/customer/edit/favoriteCompany/add")
    @Override
    public ResponseEntity<MessageDTO> addFavoriteCompany(HttpSession httpSession, FavoriteCompanyAdditionExternalDTO favoriteCompanyAdditionExternalDTO) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Expedition Search (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionCustomerRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<MemberCheckMessageDTO> securityServiceCheckCustomerSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_CUSTOMER_SESSION_URL,
                HttpMethod.POST,
                securityServiceCheckSessionCustomerRequest,
                MemberCheckMessageDTO.class
        );

        // Session Existence Clarified by Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Customer Session Exists (requestId={})", requestId);
        }

        // No User is Logged in Clarified by Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO("There is no Existing Customer Session."));
        }

        MemberCheckMessageDTO memberCheckMessageDTO = securityServiceCheckCustomerSessionResponse.getBody();
        // Something Went Wrong on Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckCustomerSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckCustomerSessionResponse.getBody().getMessage()));
        }
        FavoriteCompanyAdditionInternalDTO favoriteCompanyAdditionInternalDTO = favoriteCompanyAdditionExternalMapper
                .toFavoriteCompanyAdditionInternalDTO(favoriteCompanyAdditionExternalDTO, memberCheckMessageDTO);

        HttpEntity<FavoriteCompanyAdditionInternalDTO> memberServiceFavoriteCompanyAdditionRequest = new HttpEntity<>(favoriteCompanyAdditionInternalDTO, headers);
        ResponseEntity<MessageDTO> memberServiceFavoriteCompanyAdditionResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_FAVORITE_COMPANY_ADD_URL,
                HttpMethod.POST,
                memberServiceFavoriteCompanyAdditionRequest,
                MessageDTO.class
        );

        // Successfully Returned Expeditions from Expedition Service
        if (memberServiceFavoriteCompanyAdditionResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Expedition Successfully Retrieved (requestId={})", requestId);
        } else if (memberServiceFavoriteCompanyAdditionResponse.getStatusCode().is4xxClientError()) {
            logger.info("Bad Request for Expedition Service (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceFavoriteCompanyAdditionResponse.getStatusCode())
                    .body(new MessageDTO(memberServiceFavoriteCompanyAdditionResponse.getBody().getMessage()));
        } else if (memberServiceFavoriteCompanyAdditionResponse.getStatusCode().is5xxServerError()) {
            logger.info("Internal Server Error of Expedition Service (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceFavoriteCompanyAdditionResponse.getStatusCode())
                    .body(new MessageDTO(memberServiceFavoriteCompanyAdditionResponse.getBody().getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Success"));
    }

    @PostMapping("/customer/edit/favoriteCompany/delete")
    @Override
    public ResponseEntity<MessageDTO> deleteFavoriteCompany(HttpSession httpSession, FavoriteCompanyDeletionExternalDTO favoriteCompanyDeletionExternalDTO) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Expedition Search (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionCustomerRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<MemberCheckMessageDTO> securityServiceCheckCustomerSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_CUSTOMER_SESSION_URL,
                HttpMethod.POST,
                securityServiceCheckSessionCustomerRequest,
                MemberCheckMessageDTO.class
        );

        // Session Existence Clarified by Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Customer Session Exists (requestId={})", requestId);
        }

        // No User is Logged in Clarified by Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity
                    .status(securityServiceCheckCustomerSessionResponse.getStatusCode())
                    .body(new MessageDTO("There is no Existing Customer Session."));
        }

        MemberCheckMessageDTO memberCheckMessageDTO = securityServiceCheckCustomerSessionResponse.getBody();
        // Something Went Wrong on Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckCustomerSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckCustomerSessionResponse.getBody().getMessage()));
        }
        FavoriteCompanyDeletionInternalDTO favoriteCompanyDeletionInternalDTO = favoriteCompanyDeletionExternalMapper
                .toFavoriteCompanyDeletionInternalDTO(favoriteCompanyDeletionExternalDTO, memberCheckMessageDTO);

        HttpEntity<FavoriteCompanyDeletionInternalDTO> memberServiceFavoriteCompanyDeletionRequest = new HttpEntity<>(favoriteCompanyDeletionInternalDTO, headers);
        ResponseEntity<MessageDTO> memberServiceFavoriteCompanyDeletionResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_FAVORITE_COMPANY_DELETE_URL,
                HttpMethod.POST,
                memberServiceFavoriteCompanyDeletionRequest,
                MessageDTO.class
        );

        // Successfully Returned Expeditions from Expedition Service
        if (memberServiceFavoriteCompanyDeletionResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Expedition Successfully Retrieved (requestId={})", requestId);

        } else if (memberServiceFavoriteCompanyDeletionResponse.getStatusCode().is4xxClientError()) {
            logger.info("Bad Request for Expedition Service (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceFavoriteCompanyDeletionResponse.getStatusCode())
                    .body(new MessageDTO(memberServiceFavoriteCompanyDeletionResponse.getBody().getMessage()));

        } else if (memberServiceFavoriteCompanyDeletionResponse.getStatusCode().is5xxServerError()) {
            logger.info("Internal Server Error of Expedition Service (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceFavoriteCompanyDeletionResponse.getStatusCode())
                    .body(new MessageDTO(memberServiceFavoriteCompanyDeletionResponse.getBody().getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new MessageDTO("Success"));
    }

    @PostMapping("/customer/edit/card/add")
    @Override
    public ResponseEntity<MessageDTO> addCard(HttpSession httpSession, @RequestBody CardCreationExternalDTO cardCreationExternalDTO) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Expedition Search (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionCustomerRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<MemberCheckMessageDTO> securityServiceCheckCustomerSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_CUSTOMER_SESSION_URL,
                HttpMethod.POST,
                securityServiceCheckSessionCustomerRequest,
                MemberCheckMessageDTO.class
        );

        // Session Existence Clarified by Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Customer Session Exists (requestId={})", requestId);
        } else if (securityServiceCheckCustomerSessionResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO("There is no Existing Customer Session."));
        } else if (securityServiceCheckCustomerSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckCustomerSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckCustomerSessionResponse.getBody().getMessage()));
        }

        CardCreationInternalDTO cardCreationInternalDTO = cardCreationExternalMapper.toCardCreationInternalDTO(cardCreationExternalDTO, securityServiceCheckCustomerSessionResponse.getBody());

        HttpEntity<CardCreationInternalDTO> memberServiceCardCreationRequest = new HttpEntity<>(cardCreationInternalDTO, headers);
        ResponseEntity<MessageDTO> memberServiceCardCreationResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_CARD_ADD_URL,
                HttpMethod.POST,
                memberServiceCardCreationRequest,
                MessageDTO.class
        );

        // Successfully Saved Card on Member Service
        if (memberServiceCardCreationResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Card Successfully Saved (requestId={})", requestId);
        } else if (memberServiceCardCreationResponse.getStatusCode().is4xxClientError()) {
            logger.info("Bad Request for Member Service (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceCardCreationResponse.getStatusCode())
                    .body(memberServiceCardCreationResponse.getBody());
        } else if (memberServiceCardCreationResponse.getStatusCode().is5xxServerError()) {
            logger.info("Internal Server Error of Member Service (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceCardCreationResponse.getStatusCode())
                    .body(memberServiceCardCreationResponse.getBody());
        }
        return ResponseEntity.status(HttpStatus.OK).body(memberServiceCardCreationResponse.getBody());
    }

    @PostMapping("/customer/edit/card/delete")
    @Override
    public ResponseEntity<MessageDTO> deleteCard(HttpSession httpSession, @RequestBody CardDeletionExternalDTO cardDeletionExternalDTO) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Expedition Search (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionCustomerRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<MemberCheckMessageDTO> securityServiceCheckCustomerSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_CUSTOMER_SESSION_URL,
                HttpMethod.POST,
                securityServiceCheckSessionCustomerRequest,
                MemberCheckMessageDTO.class
        );

        // Session Existence Clarified by Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Customer Session Exists (requestId={})", requestId);
        } else if (securityServiceCheckCustomerSessionResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO("There is no Existing Customer Session."));
        } else if (securityServiceCheckCustomerSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckCustomerSessionResponse.getStatusCode())
                    .body(new MessageDTO(securityServiceCheckCustomerSessionResponse.getBody().getMessage()));
        }

        CardDeletionInternalDTO cardDeletionInternalDTO = cardDeletionExternalMapper.toCardDeletionInternalDTO(cardDeletionExternalDTO, securityServiceCheckCustomerSessionResponse.getBody());

        HttpEntity<CardDeletionInternalDTO> memberServiceCardCreationRequest = new HttpEntity<>(cardDeletionInternalDTO, headers);
        ResponseEntity<MessageDTO> memberServiceCardCreationResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_CARD_DELETE_URL,
                HttpMethod.POST,
                memberServiceCardCreationRequest,
                MessageDTO.class
        );

        // Successfully De-activated Card on Member Service
        if (memberServiceCardCreationResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Card Successfully De-Activated (requestId={})", requestId);
        } else if (memberServiceCardCreationResponse.getStatusCode().is4xxClientError()) {
            logger.info("Bad Request for Member Service (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceCardCreationResponse.getStatusCode())
                    .body(memberServiceCardCreationResponse.getBody());
        } else if (memberServiceCardCreationResponse.getStatusCode().is5xxServerError()) {
            logger.info("Internal Server Error of Member Service (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceCardCreationResponse.getStatusCode())
                    .body(memberServiceCardCreationResponse.getBody());
        }
        return ResponseEntity.status(HttpStatus.OK).body(memberServiceCardCreationResponse.getBody());
    }


    @PostMapping("/customer/get")
    @Override
    public ResponseEntity<CustomerProfileDTO> sendCustomerProfile(HttpSession httpSession) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Getting Customer Profile (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionCustomerRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<MemberCheckMessageDTO> securityServiceCheckCustomerSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_CUSTOMER_SESSION_URL,
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
                    .body(null);
        }

        // Something Went Wrong on Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckCustomerSessionResponse.getStatusCode())
                    .body(null);
        }

        MemberCheckMessageDTO memberCheckMessageDTO = securityServiceCheckCustomerSessionResponse.getBody();
        CustomerIdDTO customerIdDTO = cookieMapper.toCustomerIdDTO(memberCheckMessageDTO.getCookie());

        HttpEntity<CustomerIdDTO> memberServiceGetCustomerProfileRequest = new HttpEntity<>(customerIdDTO, headers);
        ResponseEntity<CustomerProfileDTO> memberServiceGetCustomerProfileResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_GET_CUSTOMER_PROFILE_URL,
                HttpMethod.POST,
                memberServiceGetCustomerProfileRequest,
                CustomerProfileDTO.class
        );

        // Customer Profile has been Successfully Retrieved from Member Service
        if (memberServiceGetCustomerProfileResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Customer Profile Retrieval Succeeded (requestId={})", requestId);
        } else if (memberServiceGetCustomerProfileResponse.getStatusCode().is4xxClientError()) {
            logger.info("Customer Profile Retrieval Failed (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceGetCustomerProfileResponse.getStatusCode())
                    .body(null);
        } else if (memberServiceGetCustomerProfileResponse.getStatusCode().is5xxServerError()) {
            logger.warn("Member service returned error (status={} requestId={})", memberServiceGetCustomerProfileResponse.getStatusCode(), requestId);
            return ResponseEntity
                    .status(memberServiceGetCustomerProfileResponse.getStatusCode())
                    .body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(memberServiceGetCustomerProfileResponse.getBody());
    }

    @PostMapping("/company/get")
    @Override
    public ResponseEntity<CompanyProfileDTO> sendCompanyProfile(HttpSession httpSession) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Getting Company Profile (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionCompanyRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<MemberCheckMessageDTO> securityServiceCheckCompanySessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_COMPANY_SESSION_URL,
                HttpMethod.POST,
                securityServiceCheckSessionCompanyRequest,
                MemberCheckMessageDTO.class
        );

        // There is already an Existing Session
        if (securityServiceCheckCompanySessionResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Company Session Exists (requestId={})", requestId);
        }

        // No User is Logged in Clarified by Security Service
        if (securityServiceCheckCompanySessionResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity
                    .status(securityServiceCheckCompanySessionResponse.getStatusCode())
                    .body(null);
        }

        // Something Went Wrong on Security Service
        if (securityServiceCheckCompanySessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckCompanySessionResponse.getStatusCode())
                    .body(null);
        }

        MemberCheckMessageDTO memberCheckMessageDTO = securityServiceCheckCompanySessionResponse.getBody();
        CompanyIdDTO companyIdDTO = cookieMapper.toCompanyIdDTO(memberCheckMessageDTO.getCookie());

        HttpEntity<CompanyIdDTO> memberServiceGetCompanyProfileRequest = new HttpEntity<>(companyIdDTO, headers);
        ResponseEntity<CompanyProfileDTO> memberServiceGetCompanyProfileResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_GET_COMPANY_PROFILE_URL,
                HttpMethod.POST,
                memberServiceGetCompanyProfileRequest,
                CompanyProfileDTO.class
        );

        // Company Profile has been Successfully Retrieved from Member Service
        if (memberServiceGetCompanyProfileResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Company Profile Retrieval Succeeded (requestId={})", requestId);
        } else if (memberServiceGetCompanyProfileResponse.getStatusCode().is4xxClientError()) {
            logger.info("Company Profile Retrieval Failed (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceGetCompanyProfileResponse.getStatusCode())
                    .body(null);
        } else if (memberServiceGetCompanyProfileResponse.getStatusCode().is5xxServerError()) {
            logger.warn("Member service returned error (status={} requestId={})", memberServiceGetCompanyProfileResponse.getStatusCode(), requestId);
            return ResponseEntity
                    .status(memberServiceGetCompanyProfileResponse.getStatusCode())
                    .body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(memberServiceGetCompanyProfileResponse.getBody());
    }

    @PostMapping("/admin/get")
    @Override
    public ResponseEntity<AdminProfileDTO> sendAdminProfile(HttpSession httpSession) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Getting Admin Profile (requestId={})", requestId);

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

        // No User is Logged in Clarified by Security Service
        if (securityServiceCheckAdminSessionResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity
                    .status(securityServiceCheckAdminSessionResponse.getStatusCode())
                    .body(null);
        }

        // Something Went Wrong on Security Service
        if (securityServiceCheckAdminSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckAdminSessionResponse.getStatusCode())
                    .body(null);
        }

        MemberCheckMessageDTO memberCheckMessageDTO = securityServiceCheckAdminSessionResponse.getBody();
        AdminIdDTO adminIdDTO = cookieMapper.toAdminIdDTO(memberCheckMessageDTO.getCookie());

        HttpEntity<AdminIdDTO> memberServiceGetAdminProfileRequest = new HttpEntity<>(adminIdDTO, headers);
        ResponseEntity<AdminProfileDTO> memberServiceGetAdminProfileResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_GET_ADMIN_PROFILE_URL,
                HttpMethod.POST,
                memberServiceGetAdminProfileRequest,
                AdminProfileDTO.class
        );

        // Admin Profile has been Successfully Retrieved from Member Service
        if (memberServiceGetAdminProfileResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Admin Profile Retrieval Succeeded (requestId={})", requestId);
        } else if (memberServiceGetAdminProfileResponse.getStatusCode().is4xxClientError()) {
            logger.info("Admin Profile Retrieval Failed (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceGetAdminProfileResponse.getStatusCode())
                    .body(null);
        } else if (memberServiceGetAdminProfileResponse.getStatusCode().is5xxServerError()) {
            logger.warn("Member service returned error (status={} requestId={})", memberServiceGetAdminProfileResponse.getStatusCode(), requestId);
            return ResponseEntity
                    .status(memberServiceGetAdminProfileResponse.getStatusCode())
                    .body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(memberServiceGetAdminProfileResponse.getBody());
    }

    @PostMapping("/customer/get/cards")
    @Override
    public ResponseEntity<CardsDTO> sendCustomerCards(HttpSession httpSession) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Getting Customer Cards (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckSessionCustomerRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<MemberCheckMessageDTO> securityServiceCheckCustomerSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_CUSTOMER_SESSION_URL,
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
                    .body(new CardsDTO(securityServiceCheckCustomerSessionResponse.getBody().getMessage()));
        }

        // Something Went Wrong on Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckCustomerSessionResponse.getStatusCode())
                    .body(new CardsDTO(securityServiceCheckCustomerSessionResponse.getBody().getMessage()));
        }

        MemberCheckMessageDTO memberCheckMessageDTO = securityServiceCheckCustomerSessionResponse.getBody();
        CustomerIdDTO customerIdDTO = cookieMapper.toCustomerIdDTO(memberCheckMessageDTO.getCookie());

        HttpEntity<CustomerIdDTO> expeditionServiceGetCustomerCardsRequest = new HttpEntity<>(customerIdDTO, headers);
        ResponseEntity<CardsDTO> expeditionServiceGetCustomerCardsResponse = restTemplate.exchange(
                ServiceURLs.EXPEDITION_SERVICE_GET_CUSTOMER_CARDS_URL,
                HttpMethod.POST,
                expeditionServiceGetCustomerCardsRequest,
                CardsDTO.class
        );

        // Customer Cards has been Successfully Retrieved from Expedition Service
        if (expeditionServiceGetCustomerCardsResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Customer Cards Retrieval Succeeded (requestId={})", requestId);
        } else if (expeditionServiceGetCustomerCardsResponse.getStatusCode().is4xxClientError()) {
            logger.info("Customer Cards Retrieval Failed (requestId={})", requestId);
            return ResponseEntity
                    .status(expeditionServiceGetCustomerCardsResponse.getStatusCode())
                    .body(new CardsDTO(expeditionServiceGetCustomerCardsResponse.getBody().getMessage()));
        } else if (expeditionServiceGetCustomerCardsResponse.getStatusCode().is5xxServerError()) {
            logger.warn("Expedition service returned error (status={} requestId={})", expeditionServiceGetCustomerCardsResponse.getStatusCode(), requestId);
            return ResponseEntity
                    .status(expeditionServiceGetCustomerCardsResponse.getStatusCode())
                    .body(new CardsDTO(expeditionServiceGetCustomerCardsResponse.getBody().getMessage()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(expeditionServiceGetCustomerCardsResponse.getBody());
    }
}

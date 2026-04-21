package com.shubilet.api_gateway.controllers.Impl;

import com.shubilet.api_gateway.common.constants.ServiceURLs;
import com.shubilet.api_gateway.controllers.TicketController;
import com.shubilet.api_gateway.dataTransferObjects.external.requests.expeditionOperations.BuyTicketExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.responses.ticket.TicketExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.responses.ticket.TicketsExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.CompanyIdDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.ticket.BuyTicketInternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.CookieDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.CustomerIdDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.expeditionOperations.CompanyIdNameMapDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.ticket.TicketInfoDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.auth.MemberCheckMessageDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.ticket.TicketsInternalDTO;
import com.shubilet.api_gateway.managers.HttpSessionManager;
import com.shubilet.api_gateway.mappers.CookieMapper;
import com.shubilet.api_gateway.mappers.auth.MemberCheckMessageMapper;
import com.shubilet.api_gateway.mappers.ticket.BuyTicketExternalMapper;
import com.shubilet.api_gateway.mappers.CompanyIdNameMapper;
import com.shubilet.api_gateway.mappers.ticket.TicketsInternalMapper;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ticket")
public class TicketControllerImpl implements TicketController {
    private final Logger logger = LoggerFactory.getLogger(TicketControllerImpl.class);
    private final RestTemplate restTemplate;
    private final HttpSessionManager httpSessionManager;
    private final CookieMapper cookieMapper;
    private final BuyTicketExternalMapper buyTicketExternalMapper;
    private final TicketsInternalMapper ticketsInternalMapper;

    public TicketControllerImpl(RestTemplate restTemplate, MemberCheckMessageMapper memberCheckMessageMapper,
                                CookieMapper cookieMapper, BuyTicketExternalMapper buyTicketExternalMapper,
                                TicketsInternalMapper ticketsInternalMapper) {
        this.restTemplate = restTemplate;
        this.cookieMapper = cookieMapper;
        this.buyTicketExternalMapper = buyTicketExternalMapper;
        this.ticketsInternalMapper = ticketsInternalMapper;
        this.httpSessionManager = new HttpSessionManager();

    }

    @PostMapping("/get/customer")
    public ResponseEntity<TicketsExternalDTO> sendTicketDetailsForCustomer(HttpSession httpSession) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Start Expedition Search (requestId={})", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Send Request to Security Service for Checking Existing Session
        CookieDTO cookieDTO = httpSessionManager.fromSessionToCookieDTO(httpSession);
        HttpEntity<CookieDTO> securityServiceCheckCustomerSessionRequest = new HttpEntity<>(cookieDTO, headers);
        ResponseEntity<MemberCheckMessageDTO> securityServiceCheckCustomerSessionResponse = restTemplate.exchange(
                ServiceURLs.SECURITY_SERVICE_CHECK_CUSTOMER_SESSION_URL,
                HttpMethod.POST,
                securityServiceCheckCustomerSessionRequest,
                MemberCheckMessageDTO.class);

        cookieDTO = securityServiceCheckCustomerSessionResponse.getBody().getCookie();
        httpSessionManager.updateSessionCookie(httpSession, cookieDTO);

        // Session Existence Clarified by Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Customer Session Exists (requestId={})", requestId);
        }

        // No User is Logged in Clarified by Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity
                    .status(securityServiceCheckCustomerSessionResponse.getStatusCode())
                    .body(new TicketsExternalDTO("There is no Existing Customer Session."));
        }

        // Something Went Wrong on Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity
                    .status(securityServiceCheckCustomerSessionResponse.getStatusCode())
                    .body(new TicketsExternalDTO(securityServiceCheckCustomerSessionResponse.getBody().getMessage()));
        }

        CustomerIdDTO customerIdDTO = cookieMapper.toCustomerIdDTO(cookieDTO);
        HttpEntity<CustomerIdDTO> expeditionServiceGetTicketsRequest = new HttpEntity<>(customerIdDTO, headers);
        ResponseEntity<TicketsInternalDTO> expeditionServiceGetTicketsResponse = restTemplate.exchange(
                ServiceURLs.EXPEDITION_SERVICE_GET_CUSTOMER_TICKETS_SEAT_URL,
                HttpMethod.POST,
                expeditionServiceGetTicketsRequest,
                TicketsInternalDTO.class);

        // Successfully Returned Expeditions from Expedition Service
        if (expeditionServiceGetTicketsResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Tickets Successfully Retrieved (requestId={})", requestId);

        } else if (expeditionServiceGetTicketsResponse.getStatusCode().is4xxClientError()) {
            logger.info("Bad Request for Expedition Service (requestId={})", requestId);
            return ResponseEntity
                    .status(expeditionServiceGetTicketsResponse.getStatusCode())
                    .body(new TicketsExternalDTO(expeditionServiceGetTicketsResponse.getBody().getMessage()));

        } else if (expeditionServiceGetTicketsResponse.getStatusCode().is5xxServerError()) {
            logger.info("Internal Server Error of Expedition Service (requestId={})", requestId);
            return ResponseEntity
                    .status(expeditionServiceGetTicketsResponse.getStatusCode())
                    .body(new TicketsExternalDTO(expeditionServiceGetTicketsResponse.getBody().getMessage()));
        }

        List<CompanyIdDTO> companyIdDTOs = ticketsInternalMapper
                .toCompanyIdDTOs(expeditionServiceGetTicketsResponse.getBody().getTickets());

        HttpEntity<List<CompanyIdDTO>> memberServiceGetCompanyNamesRequest = new HttpEntity<>(companyIdDTOs, headers);
        ResponseEntity<CompanyIdNameMapDTO> memberServiceGetCompanyNamesResponse = restTemplate.exchange(
                ServiceURLs.MEMBER_SERVICE_GET_COMPANY_NAMES_URL,
                HttpMethod.POST,
                memberServiceGetCompanyNamesRequest,
                CompanyIdNameMapDTO.class);

        if (memberServiceGetCompanyNamesResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Company Names Successfully Retrieved (requestId={})", requestId);

        } else if (memberServiceGetCompanyNamesResponse.getStatusCode().is4xxClientError()) {
            logger.warn("Bad Request for Member Service (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceGetCompanyNamesResponse.getStatusCode())
                    .body(new TicketsExternalDTO(memberServiceGetCompanyNamesResponse.getBody().getMessage()));

        } else if (memberServiceGetCompanyNamesResponse.getStatusCode().is5xxServerError()) {
            logger.warn("Internal Server Error of Member Service (requestId={})", requestId);
            return ResponseEntity
                    .status(memberServiceGetCompanyNamesResponse.getStatusCode())
                    .body(new TicketsExternalDTO(memberServiceGetCompanyNamesResponse.getBody().getMessage()));
        }

        List<TicketExternalDTO> ticketGetResults = CompanyIdNameMapper.toTicketsExternalDTO(
                expeditionServiceGetTicketsResponse.getBody(),
                memberServiceGetCompanyNamesResponse.getBody());
        return ResponseEntity.status(HttpStatus.OK).body(new TicketsExternalDTO("Success", ticketGetResults));
    }

    @PostMapping("/buy")
    @Override
    public ResponseEntity<TicketInfoDTO> buyTicketForCustomer(HttpSession httpSession, @RequestBody BuyTicketExternalDTO buyTicketExternalDTO) {
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
                MemberCheckMessageDTO.class);

        // Session Existence Clarified by Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Customer Session Exists (requestId={})", requestId);
        }

        // No User is Logged in Clarified by Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity.status(securityServiceCheckCustomerSessionResponse.getStatusCode())
                    .body(new TicketInfoDTO(securityServiceCheckCustomerSessionResponse.getBody().getMessage()));
        }

        // Something Went Wrong on Security Service
        if (securityServiceCheckCustomerSessionResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity.status(securityServiceCheckCustomerSessionResponse.getStatusCode())
                    .body(new TicketInfoDTO(securityServiceCheckCustomerSessionResponse.getBody().getMessage()));
        }

        BuyTicketInternalDTO buyTicketInternalDTO = buyTicketExternalMapper.toBuyTicketInternalDTO(
                buyTicketExternalDTO,
                securityServiceCheckCustomerSessionResponse.getBody());

        HttpEntity<BuyTicketInternalDTO> expeditionServiceTicketBuyRequest = new HttpEntity<>(buyTicketInternalDTO,
                headers);
        ResponseEntity<TicketInfoDTO> expeditionServiceTicketBuyResponse = restTemplate.exchange(
                ServiceURLs.EXPEDITION_SERVICE_BUY_TICKET,
                HttpMethod.POST,
                expeditionServiceTicketBuyRequest,
                TicketInfoDTO.class);

        // Session Existence Clarified by Expedition Service
        if (expeditionServiceTicketBuyResponse.getStatusCode().is2xxSuccessful()) {
            logger.info("Ticket Sale Successful (requestId={})", requestId);
        }

        // No User is Logged in Clarified by Expedition Service
        if (expeditionServiceTicketBuyResponse.getStatusCode().is4xxClientError()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(expeditionServiceTicketBuyResponse.getBody());
        }

        // Something Went Wrong on Expedition Service
        if (expeditionServiceTicketBuyResponse.getStatusCode().is5xxServerError()) {
            return ResponseEntity.status(securityServiceCheckCustomerSessionResponse.getStatusCode())
                    .body(expeditionServiceTicketBuyResponse.getBody());
        }

        return ResponseEntity.status(HttpStatus.OK).body(expeditionServiceTicketBuyResponse.getBody());
    }
}

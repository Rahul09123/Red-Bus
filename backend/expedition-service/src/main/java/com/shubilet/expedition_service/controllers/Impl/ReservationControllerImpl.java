package com.shubilet.expedition_service.controllers.Impl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import com.shubilet.expedition_service.common.constants.ServiceURLs;
import com.shubilet.expedition_service.common.enums.forReservation.ExpeditionStatus;
import com.shubilet.expedition_service.common.enums.forReservation.SeatStatus;
import com.shubilet.expedition_service.common.util.ErrorUtils;
import com.shubilet.expedition_service.common.util.StringUtils;
import com.shubilet.expedition_service.controllers.RezervationController;
import com.shubilet.expedition_service.dataTransferObjects.internal.requests.CardIdDTORequest;
import com.shubilet.expedition_service.dataTransferObjects.internal.requests.CustomerIdRequestDTO;
import com.shubilet.expedition_service.dataTransferObjects.internal.requests.TicketPaymentRequestDTO;
import com.shubilet.expedition_service.dataTransferObjects.internal.responses.CardSummaryDTO;
import com.shubilet.expedition_service.dataTransferObjects.internal.responses.TicketPaymentResponseDTO;
import com.shubilet.expedition_service.dataTransferObjects.requests.BuyTicketDTO;
import com.shubilet.expedition_service.dataTransferObjects.requests.CustomerIdDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.CardDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.TicketDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.complex.CardsDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.middle.TicketInfoDTO;
import com.shubilet.expedition_service.services.ExpeditionService;
import com.shubilet.expedition_service.services.SeatService;
import com.shubilet.expedition_service.services.TicketService;

/****

    Domain: Reservation

    Acts as the REST controller responsible for reservation-related operations, including ticket purchasing and
    retrieval of customer payment cards. This controller orchestrates complex business workflows such as validating
    expedition and seat availability, coordinating with external payment services, executing payment transactions,
    booking seats, generating tickets, and exposing customer card information. It serves as the boundary between
    HTTP requests and the reservation domain services, ensuring consistent validation, error handling, and response
    mapping across reservation use cases.

    <p>

        Technologies:

        <ul>
            <li>Spring Web</li>
            <li>SLF4J</li>
            <li>Spring RestTemplate</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @version 1.0
*/
@RestController
@RequestMapping("/api/reservation")
public class ReservationControllerImpl implements RezervationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationControllerImpl.class); 

    private final ExpeditionService expeditionService;
    private final TicketService ticketService;
    private final SeatService seatService;
    private final RestTemplate restTemplate;

    public ReservationControllerImpl(
        ExpeditionService expeditionService,
        TicketService ticketService,
        SeatService seatService,
        RestTemplate restTemplate
    ) {
        this.expeditionService = expeditionService;
        this.ticketService = ticketService;
        this.seatService = seatService;
        this.restTemplate = restTemplate;
    }

    /****

        Operation: BuyTicket

        Processes a ticket purchase request by validating the {@link BuyTicketDTO} payload, verifying expedition and seat
        availability, and ensuring the selected payment card is active via an external payment service call. If validations
        pass, calculates the payable amount, performs the payment transaction, books the requested seat, updates expedition
        capacity state, and generates a ticket PNR to retrieve full ticket details. Returns a {@link TicketInfoDTO} containing
        the booked {@link TicketDTO} and a success message, or an error response when any validation, payment, or booking step fails.

        <p>

            Uses:

            <ul>
                <li>{@link BuyTicketDTO} for customerId, expeditionId, seatNo, and cardId inputs</li>
                <li>{@link ErrorUtils} for building standardized {@link TicketInfoDTO}-based error responses</li>
                <li>{@link ExpeditionService} for expedition existence, reservability checks, pricing lookup, and seat booking updates</li>
                <li>{@link SeatService} for seat existence checks, reservability checks, seat booking, and seatId resolution</li>
                <li>{@link SeatStatus} and {@link ExpeditionStatus} for domain-level reservability outcomes</li>
                <li>{@link RestTemplate} for communicating with external payment service endpoints</li>
                <li>{@link ServiceURLs} for payment service endpoint locations</li>
                <li>{@link HttpHeaders}, {@link HttpEntity}, {@link MediaType}, and {@link HttpMethod} for HTTP request construction</li>
                <li>{@link UUID} for generating an idempotency/correlation request identifier</li>
                <li>{@link CardIdDTORequest} for card activation check requests</li>
                <li>{@link TicketPaymentRequestDTO} and {@link TicketPaymentResponseDTO} for payment execution</li>
                <li>{@link HttpStatusCodeException} for handling downstream HTTP error responses</li>
                <li>{@link TicketService} for ticket generation and ticket detail retrieval</li>
                <li>{@link TicketDTO} and {@link TicketInfoDTO} for ticket detail transport and response wrapping</li>
                <li>{@link StringUtils} for blank checks on generated PNR</li>
                <li>{@link Logger} for audit and diagnostic logging</li>
            </ul>

        </p>

        @param buyTicketDTO the purchase request containing customerId, expeditionId, seatNo, and cardId

        @return a response entity containing a {@link TicketInfoDTO} with booked ticket details and a business message,
        or an error response when the request is invalid, the seat/expedition cannot be reserved, the card is inactive,
        payment fails, or ticket generation cannot be completed
    */
    @PostMapping("/buy_ticket")
    public ResponseEntity<TicketInfoDTO> buyTicket(@RequestBody BuyTicketDTO buyTicketDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.TICKET_INFO_DTO);

        //STEP 1: Classic validation
        if(buyTicketDTO == null) {
            logger.error("BuyTicketDTO is null");
            return errorUtils.criticalError();
        }

        int customerId = buyTicketDTO.getCustomerId();
        int expeditionId = buyTicketDTO.getExpeditionId();
        int seatNo = buyTicketDTO.getSeatNo();
        int cardId = buyTicketDTO.getCardId();

        if(customerId <= 0) {
            logger.error("Invalid Customer ID: {}", customerId);
            return errorUtils.isInvalidFormat(String.valueOf(customerId));
        }

        if(expeditionId <= 0) {
            logger.error("Invalid Expedition ID: {}", expeditionId);
            return errorUtils.isInvalidFormat(String.valueOf(expeditionId));
        }

        if(seatNo <= 0) {
            logger.error("Invalid Seat Number: {}", seatNo);
            return errorUtils.isInvalidFormat(String.valueOf(seatNo));
        }

        if(cardId <= 0) {
            logger.error("Invalid Card ID: {}", cardId);
            return errorUtils.isInvalidFormat(String.valueOf(cardId));
        }
        //STEP 2: Spesific validation

        if(!expeditionService.expeditionExists(expeditionId)) {
            logger.error("Expedition not found: {}", expeditionId);
            return errorUtils.notFound("Expedition ID: " + expeditionId);
        }

        if(!seatService.seatExist(expeditionId, seatNo)) {
            logger.error("Seat not found. Expedition ID: {}, Seat No: {}", expeditionId, seatNo);
            return errorUtils.notFound("Seat No: " + seatNo + " for Expedition ID: " + expeditionId);
        }

        SeatStatus seatStatus = seatService.canBeReserved(expeditionId, seatNo);
        ExpeditionStatus expeditionStatus = expeditionService.canBeReserved(expeditionId);

        if(seatStatus != SeatStatus.SUCCESS) {
            if(seatStatus == SeatStatus.NOT_FOUND) {
                logger.error("Seat does not exist. Expedition ID: {}, Seat No: {}", expeditionId, seatNo);
                return errorUtils.notFound("Seat No: " + seatNo + " for Expedition ID: " + expeditionId);
            }

            if(seatStatus == SeatStatus.ALREADY_BOOKED) {
                logger.error("Seat already booked. Expedition ID: {}, Seat No: {}", expeditionId, seatNo);
                return errorUtils.alreadyBooked("Seat No: " + seatNo + " for Expedition ID: " + expeditionId);
            }

            logger.error("Failed to book seat. Expedition ID: {}, Seat No: {}, Status: {}", expeditionId, seatNo, seatStatus);
            return errorUtils.criticalError();
        }

        if(expeditionStatus != ExpeditionStatus.SUCCESS) {
            if(expeditionStatus == ExpeditionStatus.NOT_FOUND) {
                logger.error("Expedition does not exist: {}", expeditionId);
                return errorUtils.notFound("Expedition ID: " + expeditionId);
            }

            if(expeditionStatus == ExpeditionStatus.INVALID_TIME) {
                logger.error("Cannot book seat for past expedition time: {}", expeditionId);
                return errorUtils.isInvalidFormat("Cannot book seat for past expedition time.");
            }

            if(expeditionStatus == ExpeditionStatus.ALREADY_BOOKED) {
                logger.error("No available seats in expedition: {}", expeditionId);
                return errorUtils.alreadyBooked("Expedition ID: " + expeditionId);
            }

            logger.error("Failed to update expedition after booking seat. Expedition ID: {}, Status: {}", expeditionId, expeditionStatus);
            return errorUtils.criticalError();
        }

        // START: Payment Service communication - Card Active Check
        String requestId = UUID.randomUUID().toString();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Id", requestId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CardIdDTORequest> cardRequest = new HttpEntity<>(new CardIdDTORequest(cardId), headers);

        boolean isCardActive;

        try {
            // IMPORTANT: Boolean.class not String.class
            ResponseEntity<String> cardResponse = restTemplate.exchange(
                ServiceURLs.PAYMENT_SERVICE_CHECK_ACTIVATE,
                HttpMethod.POST,
                cardRequest,
                String.class
            );

            // If 2xx, body will be "true"/"false" (JSON boolean converted to string)
            if (!cardResponse.getStatusCode().is2xxSuccessful()) {
                // Normally won't reach here (RestTemplate throws exception on 4xx),
                // but still safe to check.
                return errorUtils.criticalError();
            }

            String body = cardResponse.getBody(); // "true" veya "false"
            isCardActive = Boolean.parseBoolean(body);

        } catch (HttpStatusCodeException ex) {
            // Falls here when 4xx/5xx occurs and body is usually {"message":"..."}
            String errorBody = ex.getResponseBodyAsString();

            // Here you have two options:
            // A) Directly consider "card not active" and proceed
            // B) Parse the message from the body and return it to the user

            // Simple and robust:
            logger.error("Card active check failed. status={}, body={}", ex.getStatusCode(), errorBody);
            return errorUtils.cardNotActive();

        } catch (Exception ex) {
            logger.error("Card active check unexpected error", ex);
            return errorUtils.criticalError();
        }

        if (!isCardActive) {
            logger.error("Card is not active. Card ID: {}", cardId);
            return errorUtils.cardNotActive();
        }

        logger.info("Card is active. Card ID: {}", cardId);

        // END: Payment Service communication - Card Active Check

        //STEP 3: Logical processing
        int amount = expeditionService.getExpeditionPrice(expeditionId);

        //START: Payment Service communication - Make Payment
        HttpEntity<TicketPaymentRequestDTO> paymentRequest = new HttpEntity<>(
            new TicketPaymentRequestDTO(cardId, String.valueOf(amount), customerId),
            headers
        );

        int paymentId;

        try {
            ResponseEntity<TicketPaymentResponseDTO> paymentResponse = restTemplate.exchange(
                ServiceURLs.PAYMENT_SERVICE_MAKE_PAYMENT,
                HttpMethod.POST,
                paymentRequest,
                TicketPaymentResponseDTO.class
            );

            if (!paymentResponse.getStatusCode().is2xxSuccessful()) {
                logger.error("Payment failed (non-2xx). Expedition ID: {}, Customer ID: {}, Seat No: {}, Status: {}",
                    expeditionId, customerId, seatNo, paymentResponse.getStatusCode());
                return errorUtils.criticalError();
            }

            TicketPaymentResponseDTO body = paymentResponse.getBody();
            if (body == null) {
                logger.error("Payment response body is null. Expedition ID: {}, Customer ID: {}, Seat No: {}",
                    expeditionId, customerId, seatNo);
                return errorUtils.criticalError();
            }

            paymentId = body.getPaymentId();
            if (paymentId <= 0) {
                logger.error("PaymentId is missing/invalid. Expedition ID: {}, Customer ID: {}, Seat No: {}, paymentId: {}",
                    expeditionId, customerId, seatNo, paymentId);
                return errorUtils.criticalError();
            }

            logger.info("Payment successful. Payment ID: {}, Expedition ID: {}, Customer ID: {}, Seat No: {}",
                paymentId, expeditionId, customerId, seatNo
            );
        }catch (HttpStatusCodeException ex) {
            logger.error("Payment failed. Expedition ID: {}, Customer ID: {}, Seat No: {}, Status: {}, Body: {}",
                expeditionId, customerId, seatNo, ex.getStatusCode(), ex.getResponseBodyAsString());

            ResponseEntity<Object> dummy = ResponseEntity.status(ex.getStatusCode()).build();
            return errorUtils.customError(dummy, "Payment failed");
        } catch (Exception ex) {
            logger.error("Payment unexpected error", ex);
            return errorUtils.criticalError();
        }
        //END: Payment Service communication - Make Payment
        
        int seatId = seatService.bookSeat(expeditionId, customerId, seatNo);
        if(seatId == -1) {
            logger.error("Failed to book seat. Expedition ID: {}, Seat No: {}", expeditionId, seatNo);
            return errorUtils.criticalError();
        }

        boolean expeditionSuccess = expeditionService.bookSeat(expeditionId);
        if(!expeditionSuccess) {
            logger.error("Failed to update expedition after booking seat. Expedition ID: {}", expeditionId);
            return errorUtils.criticalError();
        }

        String ticketPNR = ticketService.generateTicket(paymentId, seatId, customerId);
        if(StringUtils.isNullOrBlank(ticketPNR)) {
            logger.error("Failed to create ticket. Expedition ID: {}, Seat No: {}", expeditionId, seatNo);
            return errorUtils.criticalError();
        }

        TicketDTO ticketDTO = ticketService.getTicketDetails(ticketPNR);

        return ResponseEntity.ok(new TicketInfoDTO(ticketDTO, "Ticket booked successfully."));
    }

    /****

        Operation: ViewCards

        Retrieves the list of payment cards associated with the given customer by validating the incoming
        {@link CustomerIdDTO} and delegating the lookup to the external payment service. Maps the payment-service
        {@link CardSummaryDTO} array into the domain-facing {@link CardDTO} list and wraps the result in a {@link CardsDTO}.
        Returns a not-found response when the customer has no registered cards, and propagates downstream failures as
        standardized error responses.

        <p>

            Uses:

            <ul>
                <li>{@link CustomerIdDTO} for customer identifier input</li>
                <li>{@link ErrorUtils} for building standardized {@link CardsDTO}-based error responses</li>
                <li>{@link RestTemplate} for communicating with external payment service endpoints</li>
                <li>{@link ServiceURLs} for payment service endpoint locations</li>
                <li>{@link HttpHeaders}, {@link HttpEntity}, {@link MediaType}, and {@link HttpMethod} for HTTP request construction</li>
                <li>{@link CustomerIdRequestDTO} as the payment-service request payload</li>
                <li>{@link CardSummaryDTO} as the payment-service response payload</li>
                <li>{@link CardDTO} as the mapped card representation returned by this service</li>
                <li>{@link CardsDTO} as the response wrapper containing the card list</li>
                <li>{@link Arrays} and {@link List} for mapping and collection handling</li>
                <li>{@link HttpStatusCodeException} for handling downstream HTTP error responses</li>
                <li>{@link Logger} for audit and diagnostic logging</li>
            </ul>

        </p>

        @param customerIdDTO the request payload containing the customerId whose cards will be retrieved

        @return a response entity containing a {@link CardsDTO} with the customer's card summaries, or an error response
        when the input is invalid, no cards are found, or the payment service cannot be reached/returns an error
    */
    @PostMapping("/view_cards")
    public ResponseEntity<CardsDTO> viewCards(@RequestBody CustomerIdDTO customerIdDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.CARDS_DTO);

        // STEP 1: Classic validation
        if (customerIdDTO == null) {
            logger.error("CustomerIdDTO is null");
            return errorUtils.criticalError();
        }

        int customerId = customerIdDTO.getCustomerId();
        if (customerId <= 0) {
            logger.error("Invalid Customer ID: {}", customerId);
            return errorUtils.isInvalidFormat("Customer Id");
        }

        // STEP 2: Specific validation
        // (none – intentional)

        // Mirliva says: The first rule of reservation service is:
        // you do not double-book the same seat.

        // STEP 3: Business Logic (Payment-Service call)
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<CustomerIdRequestDTO> request =
                new HttpEntity<>(new CustomerIdRequestDTO(customerId), headers);

            ResponseEntity<List<CardSummaryDTO>> response = restTemplate.exchange(
                ServiceURLs.PAYMENT_SERVICE_CUSTOMER_CARDS,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<List<CardSummaryDTO>>() {}
            );

            List<CardSummaryDTO> body = response.getBody();

            if (body == null || body.isEmpty()) {
                logger.info("No cards found for customerId={}", customerId);
                return errorUtils.notFound("Cards");
            }

            // Map Payment DTO → Expedition DTO
            List<CardDTO> cards = body.stream()
                .map(c -> new CardDTO(
                    Integer.parseInt(c.getCardId()),   // String → int
                    c.getLast4Digits(),
                    c.getExpirationMonth(),
                    c.getExpirationYear()
                ))
                .toList();

            logger.info("Cards found for customerId={}", customerId);
            return ResponseEntity.ok(new CardsDTO("Cards found", cards));

        } catch (HttpStatusCodeException ex) {
            // Payment-service 4xx/5xx
            logger.error(
                "Payment-service error while fetching cards. customerId={}, status={}, body={}",
                customerId,
                ex.getStatusCode(),
                ex.getResponseBodyAsString()
            );

            ResponseEntity<Object> dummy =
                ResponseEntity.status(ex.getStatusCode()).build();

            return errorUtils.customError(dummy, "Cards could not be fetched");

        } catch (Exception ex) {
            logger.error("Unexpected error while fetching cards", ex);
            return errorUtils.criticalError();
        }
    }
    
}

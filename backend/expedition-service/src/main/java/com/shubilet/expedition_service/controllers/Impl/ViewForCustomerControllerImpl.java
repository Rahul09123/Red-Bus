package com.shubilet.expedition_service.controllers.Impl;

import java.time.Instant;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.shubilet.expedition_service.common.util.ErrorUtils;
import com.shubilet.expedition_service.common.util.StringUtils;
import com.shubilet.expedition_service.common.util.ValidationUtils;
import com.shubilet.expedition_service.controllers.ViewForCustomerController;
import com.shubilet.expedition_service.dataTransferObjects.requests.CustomerIdDTO;
import com.shubilet.expedition_service.dataTransferObjects.requests.ExpeditionIdDTO;
import com.shubilet.expedition_service.dataTransferObjects.requests.ViewDetailsForCustomerDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.ExpeditionForCustomerDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.SeatForCustomerDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.TicketDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.complex.ExpeditionsForCustomerDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.complex.SeatsForCustomerDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.complex.TicketsDTO;
import com.shubilet.expedition_service.services.ExpeditionService;
import com.shubilet.expedition_service.services.SeatService;
import com.shubilet.expedition_service.services.TicketService;

/****

    Domain: View

    Provides customer-facing read-only REST endpoints for discovering expeditions, browsing available seats, and
    retrieving ticket history. This controller validates customer query criteria such as route, date, and identifiers,
    enforces basic business constraints (e.g., non-past travel dates and expedition existence), and delegates data
    retrieval to the underlying expedition, seat, and ticket services. It returns DTO-based responses suitable for
    customer UI consumption, including empty-result payloads where applicable.

    <p>

        Technologies:

        <ul>
            <li>Spring Web</li>
            <li>SLF4J</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @version 1.0
*/
@RestController
@RequestMapping("/api/view/customer")
public class ViewForCustomerControllerImpl implements ViewForCustomerController {

    private static final Logger logger = LoggerFactory.getLogger(ViewForCustomerControllerImpl.class);

    private final ExpeditionService expeditionService;
    private final SeatService seatService;
    private final TicketService ticketService;

    public ViewForCustomerControllerImpl(
        ExpeditionService expeditionService,
        SeatService seatService,
        TicketService ticketService
    ) {
        this.expeditionService = expeditionService;
        this.seatService = seatService;
        this.ticketService = ticketService;
    }

    /****

        Operation: ViewAvailable

        Retrieves available expeditions for customers based on the provided route and date criteria. Validates the
        incoming {@link ViewDetailsForCustomerDTO} by checking departure and arrival cities, ensuring they are not the
        same, and verifying that the requested date is well-formed and not in the past. Delegates the lookup to the
        expedition service to fetch expeditions matching the route and date, and returns the results in an
        {@link ExpeditionsForCustomerDTO}.

        <p>

            Uses:

            <ul>
                <li>{@link ViewDetailsForCustomerDTO} for departureCity, arrivalCity, and date input</li>
                <li>{@link ErrorUtils} for building standardized {@link ExpeditionsForCustomerDTO}-based error responses</li>
                <li>{@link StringUtils} for null/blank and equality checks</li>
                <li>{@link ValidationUtils} for date format and temporal validation</li>
                <li>{@link Instant} for current time comparison</li>
                <li>{@link ExpeditionService} for querying expeditions by route and date</li>
                <li>{@link ExpeditionForCustomerDTO} as the per-expedition response representation</li>
                <li>{@link ExpeditionsForCustomerDTO} as the response wrapper containing expedition results</li>
                <li>{@link Logger} for audit and diagnostic logging</li>
            </ul>

        </p>

        @param viewDetailsForCustomerDTO the request payload containing departure city, arrival city, and travel date

        @return a response entity containing an {@link ExpeditionsForCustomerDTO} with available expeditions for the
        specified route and date, or an error response when the request is invalid
    */
    @PostMapping("/availableExpeditions")
    public ResponseEntity<ExpeditionsForCustomerDTO> viewAvailableExpeditions(@RequestBody ViewDetailsForCustomerDTO viewDetailsForCustomerDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.EXPEDITIONS_FOR_CUSTOMER_DTO);

        //STEP 1 : Classic validation
        if(viewDetailsForCustomerDTO == null) {
            logger.error("ViewDetailsForCustomerDTO is null");
            return errorUtils.criticalError();
        }

        String departureCity = viewDetailsForCustomerDTO.getDepartureCity();
        String arrivalCity = viewDetailsForCustomerDTO.getArrivalCity();
        String date = viewDetailsForCustomerDTO.getDate();

        if(StringUtils.isNullOrBlank(departureCity)) {
            logger.error("Departure City is null or blank");
            return errorUtils.isNull("Departure City");
        }

        if(StringUtils.isNullOrBlank(arrivalCity)) {
            logger.error("Arrival City is null or blank");
            return errorUtils.isNull("Arrival City");
        }

        if(StringUtils.isNullOrBlank(date)) {
            logger.error("Date is null or blank");
            return errorUtils.isNull("Date");
        }

        if(!ValidationUtils.isValidDate(date)) {
            logger.error("Date format is invalid: {}", date);
            return errorUtils.isInvalidFormat("Date");
        }

        //STEP 2 : Spesific validation
        if(StringUtils.nullSafeEquals(arrivalCity, departureCity)) {
            logger.error("Arrival City and Departure City are the same: {}", arrivalCity);
            return errorUtils.sameCityError();
        }
        
        Instant now = Instant.now();
        if(!ValidationUtils.isDateNotInPast(date, now)) {
            logger.error("Date is in the past: {}", date);
            return errorUtils.dateInPastError();
        }

        //STEP 3 : Business Logic
        List<ExpeditionForCustomerDTO> expeditions = expeditionService.findExpeditionsByInstantAndRoute(departureCity, arrivalCity, date);
        
        if(expeditions.isEmpty()) {
            logger.info("No expeditions found for route {} to {} on date {}", departureCity, arrivalCity, date);
            return ResponseEntity.ok(new ExpeditionsForCustomerDTO("No expeditions found", expeditions));
        }

        logger.info("Found {} expeditions for route {} to {} on date {}", expeditions.size(), departureCity, arrivalCity, date);
        return ResponseEntity.ok(new ExpeditionsForCustomerDTO("Expeditions found", expeditions));
    }

    /****

        Operation: ViewAvailableSeats

        Retrieves the list of currently available seats for a specific expedition. Validates the incoming
        {@link ExpeditionIdDTO} to ensure a valid expedition identifier is provided and verifies that the expedition
        exists before delegating the lookup to the seat service. Returns a {@link SeatsForCustomerDTO} containing
        available seat information when seats are found, or a standardized error response when the request is invalid,
        the expedition does not exist, or no seats are available.

        <p>

            Uses:

            <ul>
                <li>{@link ExpeditionIdDTO} for expedition identifier input</li>
                <li>{@link ErrorUtils} for building standardized {@link SeatsForCustomerDTO}-based error responses</li>
                <li>{@link ExpeditionService} for expedition existence validation</li>
                <li>{@link SeatService} for retrieving available seats by expeditionId</li>
                <li>{@link SeatForCustomerDTO} as the per-seat response representation</li>
                <li>{@link SeatsForCustomerDTO} as the response wrapper containing available seats</li>
                <li>{@link Logger} for audit and diagnostic logging</li>
            </ul>

        </p>

        @param expeditionIdDTO the request payload containing the expeditionId whose available seats will be retrieved

        @return a response entity containing a {@link SeatsForCustomerDTO} with available seats for the expedition,
        or an error response when the request is invalid, the expedition is not found, or no seats are available
    */

    @PostMapping("/availableSeats")
    public ResponseEntity<SeatsForCustomerDTO> viewAvailableSeats(@RequestBody ExpeditionIdDTO expeditionIdDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.SEATS_FOR_CUSTOMER_DTO);

        //STEP 1 : Classic validation
        if(expeditionIdDTO == null) {
            logger.error("ExpeditionIdDTO is null");
            return errorUtils.criticalError();
        }

        int expeditionId = expeditionIdDTO.getExpeditionId();

        if(expeditionId <= 0) {
            logger.error("Expedition Id is invalid: {}", expeditionId);
            return errorUtils.isNull("Expedition Id");
        }

        //STEP 2 : Spesific validation
        if(!expeditionService.expeditionExists(expeditionId)) {
            logger.error("Expedition not found for Id: {}", expeditionId);
            return errorUtils.notFound("Expedition");
        }

        //STEP 3 : Business Logic
        List<SeatForCustomerDTO> availableSeats = seatService.getByAvailableSeats(expeditionId);

        if(availableSeats.isEmpty()) {
            logger.info("No available seats found for expedition Id {}", expeditionId);
            return errorUtils.notFound("Available seats");
        }

        logger.info("Found {} available seats for expedition Id {}", availableSeats.size(), expeditionId);
        return ResponseEntity.ok(new SeatsForCustomerDTO("Available seats found", availableSeats));
    }

    /****

        Operation: ViewAllTickets

        Retrieves all tickets associated with the specified customer. Validates the incoming {@link CustomerIdDTO}
        to ensure a valid customer identifier is provided, then delegates the lookup to the ticket service to fetch
        all tickets owned by the customer. Returns a {@link TicketsDTO} containing the customer’s tickets when found,
        or an empty-result response when no tickets exist for the given customer.

        <p>

            Uses:

            <ul>
                <li>{@link CustomerIdDTO} for customer identifier input</li>
                <li>{@link ErrorUtils} for building standardized {@link TicketsDTO}-based error responses</li>
                <li>{@link TicketService} for retrieving tickets by customerId</li>
                <li>{@link TicketDTO} as the per-ticket response representation</li>
                <li>{@link TicketsDTO} as the response wrapper containing ticket results</li>
                <li>{@link Logger} for audit and diagnostic logging</li>
            </ul>

        </p>

        @param customerIdDTO the request payload containing the customerId whose tickets will be retrieved

        @return a response entity containing a {@link TicketsDTO} with all tickets for the customer, or an empty-result
        response when the customer has no tickets
    */
   
    @PostMapping("/allTickets")
    public ResponseEntity<TicketsDTO> viewAllTickets(@RequestBody CustomerIdDTO customerIdDTO) {
        ErrorUtils errorUtils = new ErrorUtils(ErrorUtils.ConversionType.TICKETS_DTO);

        //STEP 1 : Classic validation
        if(customerIdDTO == null) {
            logger.error("CustomerIdDTO is null");
            return errorUtils.criticalError();
        }

        int customerId = customerIdDTO.getCustomerId();

        if(customerId <= 0) {
            logger.error("Customer Id is invalid: {}", customerId);
            return errorUtils.isNull("Customer Id");
        }
        //STEP 2 : Spesific validation
        

        //STEP 3 : Business Logic

        List<TicketDTO> tickets = ticketService.getTicketsByCustomerId(customerId);

        if(tickets.isEmpty()) {
            logger.info("No tickets found for customer Id {}", customerId);
            return ResponseEntity.ok(new TicketsDTO("No tickets found", tickets));
        }

        logger.info("Found {} tickets for customer Id {}", tickets.size(), customerId);
        return ResponseEntity.ok(new TicketsDTO("Tickets retrieved successfully", tickets));
    }
}

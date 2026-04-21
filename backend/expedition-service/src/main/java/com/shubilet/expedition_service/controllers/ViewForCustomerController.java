package com.shubilet.expedition_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shubilet.expedition_service.dataTransferObjects.requests.CustomerIdDTO;
import com.shubilet.expedition_service.dataTransferObjects.requests.ExpeditionIdDTO;
import com.shubilet.expedition_service.dataTransferObjects.requests.ViewDetailsForCustomerDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.ExpeditionForCustomerDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.SeatForCustomerDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.TicketDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.complex.ExpeditionsForCustomerDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.complex.SeatsForCustomerDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.complex.TicketsDTO;

/****

    Domain: View

    Declares the REST API contract for customer-scoped read-only operations under the
    {@code /api/view/customer} resource. This interface defines endpoints that allow customers
    to discover available expeditions based on route and date, inspect available seats for a
    selected expedition, and retrieve their complete ticket history. Implementations are
    responsible for validating request criteria, enforcing business constraints, and delegating
    data retrieval to the appropriate expedition, seat, and ticket services.

    <p>

        Technologies:

        <ul>
            <li>Spring Web</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @version 1.0
*/
@RestController
@RequestMapping("/api/view/customer")
public interface ViewForCustomerController {

    /****

        Operation: ViewAvailable

        Defines the contract for retrieving available expeditions for customers based on route and date criteria.
        Implementations are responsible for validating the provided {@link ViewDetailsForCustomerDTO}, ensuring that
        departure and arrival cities are present and distinct, and that the requested date is valid. The underlying
        expedition data source is queried to return expeditions matching the given route and travel date.

        <p>

            Usage:

            <pre>

                POST /api/view/customer/availableExpeditions

                Request Body:
                {
                    "departureCity": "Ankara",
                    "arrivalCity": "Istanbul",
                    "date": "2025-01-20"
                }

                Response:
                {
                    "message": "Expeditions found",
                    "expeditions": [
                        {
                            "expeditionId": 42,
                            "departureCity": "Ankara",
                            "arrivalCity": "Istanbul",
                            "date": "2025-01-20",
                            "time": "14:30",
                            "price": 350.0,
                            "duration": 300,
                            "companyId": 3
                        }
                    ]
                }

            </pre>
        </p>

        <p>

            Uses:

            <ul>
                <li>{@link ViewDetailsForCustomerDTO} as the request payload carrying route and date criteria</li>
                <li>{@link ExpeditionsForCustomerDTO} as the response wrapper containing expedition results</li>
                <li>{@link ExpeditionForCustomerDTO} as the per-expedition response representation</li>
                <li>{@link ResponseEntity} for HTTP-level response abstraction</li>
            </ul>

        </p>

        @param viewDetailsForCustomerDTO the request payload containing departure city, arrival city, and travel date

        @return a response entity containing an {@link ExpeditionsForCustomerDTO} with available expeditions for the given criteria
    */
    @PostMapping("/availableExpeditions")
    public ResponseEntity<ExpeditionsForCustomerDTO> viewAvailableExpeditions(ViewDetailsForCustomerDTO viewDetailsForCustomerDTO);

    /****

        Operation: ViewAvailableSeats
        Defines the contract for retrieving all available seats for a specific expedition identified by the provided
        {@link ExpeditionIdDTO}. Implementations are responsible for validating the expedition identifier, ensuring
        the expedition exists, and querying the underlying seat data source to return seats that are currently
        available for reservation.

        <p>

            Usage:

            <pre>

                POST /api/view/customer/availableSeats

                Request Body:
                {
                    "expeditionId": 42
                }

                Response:
                {
                    "message": "Available seats found",
                    "tickets": [
                        {
                            "expeditionId": 42,
                            "seatNo": 8,
                            "status": "AVAILABLE"
                        }
                    ]
                }

            </pre>
        </p>

        <p>

            Uses:

            <ul>
                <li>{@link ExpeditionIdDTO} as the request payload carrying the expedition identifier</li>
                <li>{@link SeatsForCustomerDTO} as the response wrapper containing available seat results</li>
                <li>{@link SeatForCustomerDTO} as the per-seat response representation</li>
                <li>{@link ResponseEntity} for HTTP-level response abstraction</li>
            </ul>

        </p>

        @param expeditionIdDTO the request payload containing the expeditionId whose available seats will be retrieved

        @return a response entity containing a {@link SeatsForCustomerDTO} with available seats for the expedition
    */
    @PostMapping("/availableSeats")
    public ResponseEntity<SeatsForCustomerDTO> viewAvailableSeats(ExpeditionIdDTO expeditionIdDTO);
    
    /****

        Operation: ViewAllTickets

        Defines the contract for retrieving all tickets associated with a specific customer identified by the provided
        {@link CustomerIdDTO}. Implementations are responsible for validating the customer identifier, querying the
        underlying ticket data source, and returning all tickets owned by the customer, including full expedition
        and journey details for each ticket.

        <p>

            Usage:

            <pre>

                POST /api/view/customer/allTickets

                Request Body:
                {
                    "customerId": 12
                }

                Response:
                {
                    "message": "Tickets retrieved successfully",
                    "ticketsDTO": [
                        {
                            "PNR": "ABC123XYZ",
                            "seatNo": 8,
                            "expeditionId": 42,
                            "companyId": 3,
                            "departureCity": "Ankara",
                            "arrivalCity": "Istanbul",
                            "date": "2025-01-20",
                            "time": "14:30",
                            "duration": 300
                        }
                    ]
                }

            </pre>
        </p>

        <p>

            Uses:

            <ul>
                <li>{@link CustomerIdDTO} as the request payload carrying the customer identifier</li>
                <li>{@link TicketsDTO} as the response wrapper containing ticket results</li>
                <li>{@link TicketDTO} as the detailed ticket representation</li>
                <li>{@link ResponseEntity} for HTTP-level response abstraction</li>
            </ul>

        </p>

        @param customerIdDTO the request payload containing the customerId whose tickets will be retrieved

        @return a response entity containing a {@link TicketsDTO} with all tickets belonging to the customer
    */
    @PostMapping("/allTickets")
    public ResponseEntity<TicketsDTO> viewAllTickets(CustomerIdDTO customerIdDTO);
}

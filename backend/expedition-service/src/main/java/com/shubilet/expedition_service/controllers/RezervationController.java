package com.shubilet.expedition_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shubilet.expedition_service.controllers.Impl.ReservationControllerImpl;
import com.shubilet.expedition_service.dataTransferObjects.requests.BuyTicketDTO;
import com.shubilet.expedition_service.dataTransferObjects.requests.CustomerIdDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.CardDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.TicketDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.complex.CardsDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.middle.TicketInfoDTO;

/****

    Domain: Reservation

    Declares the REST API contract for reservation-related operations under the {@code /api/reservation} resource.
    This interface defines endpoints for purchasing tickets and retrieving customer payment card information.
    Implementations are responsible for orchestrating validation, availability checks, payment coordination,
    ticket generation, and integration with external payment services, while returning standardized DTO-based
    responses suitable for client consumption.

    <p>

        Technologies:

        <ul>
            <li>Spring Web</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @see ReservationControllerImpl

    @version 1.0
*/
@RestController
@RequestMapping("/api/reservation")
public interface RezervationController {

    /****

        Operation: BuyTicket

        Defines the contract for purchasing a ticket for a specific expedition by accepting customer, expedition,
        seat, and payment card information encapsulated in {@link BuyTicketDTO}. Implementations are responsible for
        validating input data, checking expedition and seat availability, coordinating payment processing, booking
        the seat, generating a ticket identifier (PNR), and returning detailed ticket information upon success.

        <p>

            Usage:

            <pre>

                POST /api/reservation/buy_ticket

                Request Body:
                {
                    "customerId": 12,
                    "expeditionId": 45,
                    "seatNo": 8,
                    "cardId": 102
                }

                Response:
                {
                    "message": "Ticket booked successfully.",
                    "ticketDTO": {
                        "PNR": "ABC123XYZ",
                        "seatNo": 8,
                        "expeditionId": 45,
                        "companyId": 3,
                        "departureCity": "Ankara",
                        "arrivalCity": "Istanbul",
                        "date": "2025-01-20",
                        "time": "14:30",
                        "duration": 300
                    }
                }

            </pre>
        </p>

        <p>

            Uses:

            <ul>
                <li>{@link BuyTicketDTO} as the request payload carrying ticket purchase parameters</li>
                <li>{@link TicketInfoDTO} as the response wrapper containing ticket details and a business message</li>
                <li>{@link TicketDTO} as the detailed ticket representation returned upon successful purchase</li>
                <li>{@link ResponseEntity} for HTTP-level response abstraction</li>
            </ul>

        </p>

        @param buyTicketDTO the ticket purchase request containing customerId, expeditionId, seatNo, and cardId

        @return a response entity containing a {@link TicketInfoDTO} with ticket details and operation status
    */
    @PostMapping("/buy_ticket")
    public ResponseEntity<TicketInfoDTO> buyTicket(BuyTicketDTO buyTicketDTO);

    /****

        Operation: ViewCards

        Defines the contract for retrieving all payment cards associated with a specific customer by accepting the
        customer identifier encapsulated in {@link CustomerIdDTO}. Implementations are responsible for validating the
        customer identity, querying the underlying payment or card service, and returning a {@link CardsDTO} containing
        the customer’s registered cards along with a descriptive business message.

        <p>

            Usage:

            <pre>

                POST /api/reservation/view_cards

                Request Body:
                {
                    "customerId": 12
                }

                Response:
                {
                    "message": "Cards found",
                    "cards": [
                        {
                            "cardId": 101,
                            "cardNo": "**** **** **** 1234",
                            "name": "John",
                            "surname": "Doe"
                        }
                    ]
                }

            </pre>
        </p>

        <p>

            Uses:

            <ul>
                <li>{@link CustomerIdDTO} as the request payload carrying the customer identifier</li>
                <li>{@link CardsDTO} as the response wrapper containing card information</li>
                <li>{@link CardDTO} as the individual card representation</li>
                <li>{@link ResponseEntity} for HTTP-level response abstraction</li>
            </ul>

        </p>

        @param customerIdDTO the request payload containing the customerId whose cards will be retrieved

        @return a response entity containing a {@link CardsDTO} with the customer’s card list and a business message
    */
    @PostMapping("/view_cards")
    public ResponseEntity<CardsDTO> viewCards(CustomerIdDTO customerIdDTO);
}

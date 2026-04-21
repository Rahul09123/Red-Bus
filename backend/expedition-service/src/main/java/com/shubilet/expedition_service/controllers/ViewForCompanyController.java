package com.shubilet.expedition_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shubilet.expedition_service.dataTransferObjects.requests.CompanyIdDTO;
import com.shubilet.expedition_service.dataTransferObjects.requests.ExpeditionViewByDateDTO;
import com.shubilet.expedition_service.dataTransferObjects.requests.ExpeditionViewByIdDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.ExpeditionForCompanyDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.SeatForCompanyDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.complex.ExpeditionsForCompanyDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.complex.SeatsForCompanyDTO;

/****

    Domain: View

    Defines the REST API contract for company-scoped expedition viewing operations. This interface exposes
    read-only endpoints that allow companies to query their expeditions by date, retrieve all upcoming
    (active) expeditions, list all expeditions regardless of status, and inspect detailed seat-level
    information for a specific expedition. Implementations are responsible for validating company context,
    enforcing access boundaries, aggregating business metrics, and delegating data retrieval to the
    underlying expedition and seat services.

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
@RequestMapping("/api/view/company")
public interface ViewForCompanyController {

    /****

        Operation: ViewByDate

        Defines the contract for retrieving expeditions belonging to a specific company filtered by a given date.
        Implementations are responsible for validating the provided {@link ExpeditionViewByDateDTO}, ensuring the
        company context is valid, and querying the underlying expedition data source to return all matching expeditions
        scheduled on the specified date. The result includes detailed expedition information along with aggregated
        business metrics such as booked seats and profit.

        <p>

            Usage:

            <pre>

                POST /api/view/company/expeditionsByDate

                Request Body:
                {
                    "companyId": 5,
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
                            "capacity": 40,
                            "numberOfBookedSeats": 25,
                            "profit": 8750.0
                        }
                    ]
                }

            </pre>
        </p>

        <p>

            Uses:

            <ul>
                <li>{@link ExpeditionViewByDateDTO} as the request payload carrying companyId and date filter</li>
                <li>{@link ExpeditionsForCompanyDTO} as the response wrapper containing expedition results</li>
                <li>{@link ExpeditionForCompanyDTO} as the detailed per-expedition representation</li>
                <li>{@link ResponseEntity} for HTTP-level response abstraction</li>
            </ul>

        </p>

        @param expeditionViewByDateDTO the request payload containing the companyId and date to filter expeditions

        @return a response entity containing an {@link ExpeditionsForCompanyDTO} with expeditions for the specified date
    */
    @PostMapping("/expeditionsByDate")
    public ResponseEntity<ExpeditionsForCompanyDTO> viewExpeditionsByDate(ExpeditionViewByDateDTO expeditionViewByDateDTO);

    /****

        Operation: ViewActive

        Defines the contract for retrieving all upcoming (active) expeditions belonging to a specific company.
        Implementations are responsible for validating the provided {@link CompanyIdDTO}, ensuring the company
        context is valid, and querying the underlying expedition data source to return expeditions that have not
        yet been completed. The response includes detailed expedition information along with aggregated business
        metrics such as booked seat counts and generated profit.

        <p>

            Usage:

            <pre>

                POST /api/view/company/activeExpeditions

                Request Body:
                {
                    "companyId": 5
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
                            "capacity": 40,
                            "numberOfBookedSeats": 25,
                            "profit": 8750.0
                        }
                    ]
                }

            </pre>
        </p>

        <p>

            Uses:

            <ul>
                <li>{@link CompanyIdDTO} as the request payload carrying the company identifier</li>
                <li>{@link ExpeditionsForCompanyDTO} as the response wrapper containing expedition results</li>
                <li>{@link ExpeditionForCompanyDTO} as the detailed per-expedition representation</li>
                <li>{@link ResponseEntity} for HTTP-level response abstraction</li>
            </ul>

        </p>

        @param companyIdDTO the request payload containing the companyId whose active expeditions will be retrieved

        @return a response entity containing an {@link ExpeditionsForCompanyDTO} with upcoming expeditions for the company
    */
    @PostMapping("/activeExpeditions")
    public ResponseEntity<ExpeditionsForCompanyDTO> viewActiveExpeditions(CompanyIdDTO companyIdDTO);

    /****

        Operation: ViewAll

        Defines the contract for retrieving all expeditions associated with a specific company, regardless of their
        current status (past, active, or upcoming). Implementations are responsible for validating the provided
        {@link CompanyIdDTO}, ensuring the company context is valid, and querying the underlying expedition data
        source to return the complete list of expeditions owned by the company. The response includes detailed
        expedition information along with aggregated business metrics such as booked seat counts and profit.

        <p>

            Usage:

            <pre>

                POST /api/view/company/allExpeditions

                Request Body:
                {
                    "companyId": 5
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
                            "capacity": 40,
                            "numberOfBookedSeats": 25,
                            "profit": 8750.0
                        }
                    ]
                }

            </pre>
        </p>

        <p>

            Uses:

            <ul>
                <li>{@link CompanyIdDTO} as the request payload carrying the company identifier</li>
                <li>{@link ExpeditionsForCompanyDTO} as the response wrapper containing expedition results</li>
                <li>{@link ExpeditionForCompanyDTO} as the detailed per-expedition representation</li>
                <li>{@link ResponseEntity} for HTTP-level response abstraction</li>
            </ul>

        </p>

        @param companyIdDTO the request payload containing the companyId whose expeditions will be retrieved

        @return a response entity containing an {@link ExpeditionsForCompanyDTO} with all expeditions for the company
    */
    @PostMapping("/allExpeditions")
    public ResponseEntity<ExpeditionsForCompanyDTO> viewAllExpeditions(CompanyIdDTO companyIdDTO);

    /****

        Operation: ViewDetails

        Defines the contract for retrieving detailed seat information for a specific expedition belonging to a given
        company. Implementations are responsible for validating the provided {@link ExpeditionViewByIdDTO}, ensuring
        both company and expedition identifiers are valid and related, and querying the underlying seat data source
        to return seat-level details including reservation status and customer associations.

        <p>

            Usage:

            <pre>

                POST /api/view/company/expeditionDetails

                Request Body:
                {
                    "companyId": 5,
                    "expeditionId": 42
                }

                Response:
                {
                    "message": "Expedition details found",
                    "tickets": [
                        {
                            "seatId": 101,
                            "expeditionId": 42,
                            "seatNo": 8,
                            "customerId": 12,
                            "status": "BOOKED"
                        }
                    ]
                }

            </pre>
        </p>

        <p>

            Uses:

            <ul>
                <li>{@link ExpeditionViewByIdDTO} as the request payload carrying companyId and expeditionId</li>
                <li>{@link SeatsForCompanyDTO} as the response wrapper containing seat details</li>
                <li>{@link SeatForCompanyDTO} as the per-seat response representation</li>
                <li>{@link ResponseEntity} for HTTP-level response abstraction</li>
            </ul>

        </p>

        @param expeditionViewById the request payload containing the companyId and expeditionId to retrieve seat details for

        @return a response entity containing a {@link SeatsForCompanyDTO} with seat information for the expedition
    */
    @PostMapping("/expeditionDetails")
    public ResponseEntity<SeatsForCompanyDTO> viewExpeditionDetails(ExpeditionViewByIdDTO expeditionViewById);
}

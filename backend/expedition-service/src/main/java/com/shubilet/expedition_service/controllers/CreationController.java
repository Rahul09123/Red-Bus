package com.shubilet.expedition_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shubilet.expedition_service.controllers.Impl.CreationControllerImpl;
import com.shubilet.expedition_service.dataTransferObjects.requests.ExpeditionCreationDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.message.MessageDTO;

/****

    Domain: Expedition

    Declares the REST API contract for expedition creation operations under the {@code /api/expeditions} resource.
    This interface defines the entry point for creating new expeditions by accepting route, schedule, pricing,
    capacity, and company ownership information. Implementations are responsible for enforcing business rules,
    validating request integrity, persisting expedition data, and returning standardized response messages that
    reflect the outcome of the creation process.

    <p>

        Technologies:

        <ul>
            <li>Spring Web</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @see CreationControllerImpl

    @version 1.0
*/
@RestController
@RequestMapping("/api/expeditions")
public interface CreationController {

    /****

        Operation: Create

        Defines the contract for creating a new expedition by accepting route, schedule, pricing, capacity, and
        company information encapsulated in {@link ExpeditionCreationDTO}. Implementations are responsible for
        validating business rules (such as city consistency, date validity, capacity limits, and company ownership),
        persisting the expedition, and returning a standardized {@link MessageDTO} indicating the outcome.

        <p>

            Usage:

            <pre>

                POST /api/expeditions/create

                Request Body:
                {
                    "departureCity": "Ankara",
                    "arrivalCity": "Istanbul",
                    "date": "2025-01-20",
                    "time": "14:30",
                    "price": 350.0,
                    "duration": 300,
                    "companyId": 5,
                    "capacity": 40
                }

                Response:
                {
                    "message": "Expedition created successfully."
                }

            </pre>
        </p>

        <p>

            Uses:

            <ul>
                <li>{@link ExpeditionCreationDTO} as the request payload carrying expedition configuration</li>
                <li>{@link MessageDTO} as the standardized response wrapper conveying operation status</li>
                <li>{@link ResponseEntity} for HTTP-level response abstraction</li>
            </ul>

        </p>

        @param request the expedition creation request containing route, schedule, pricing, capacity, and company details

        @return a response entity containing a {@link MessageDTO} describing the result of the creation operation
    */
    @PostMapping("/create")
    public ResponseEntity<MessageDTO> createExpedition(ExpeditionCreationDTO request);
}

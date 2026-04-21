/**
 * CardController defines the HTTP-level contract for managing customer payment cards.
 *
 * <p>
 * This interface exposes endpoints for creating, retrieving, deactivating,
 * and validating payment cards within the payment-service.
 * It acts as the entry point for client requests related to card management
 * and delegates business logic execution to the corresponding service layer.
 * </p>
 *
 * <p>
 * Responsibilities include:
 * <ul>
 *   <li>Accepting and validating incoming card-related requests</li>
 *   <li>Providing summarized card data without exposing sensitive information</li>
 *   <li>Returning standardized HTTP responses using {@link ResponseEntity}</li>
 *   <li>Ensuring clear separation between controller and service layers</li>
 * </ul>
 * </p>
 *
 * <p>
 * This interface is implemented by a concrete controller class that handles
 * request mappings, validation handling, and response construction.
 * </p>
 *
 * @author Ömer Tahsin Taşkın -  <a href="https://github.com/omertahsintaskin">https://github.com/omertahsintaskin</a>
 * @version 1.0
 * @since 2025-12
 */


package com.shubilet.payment_service.controllers;

import com.shubilet.payment_service.dataTransferObjects.requests.CardDTO;
import com.shubilet.payment_service.dataTransferObjects.requests.CardDeactivationRequestDTO;
import com.shubilet.payment_service.dataTransferObjects.requests.CardIdRequestDTO;
import com.shubilet.payment_service.dataTransferObjects.requests.CustomerIdRequestDTO;
import com.shubilet.payment_service.dataTransferObjects.responses.CardSummaryDTO;
import com.shubilet.payment_service.dataTransferObjects.responses.MessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface CardController {

    /**
     * Saves a new payment card for a customer after validating the provided card
     * data.
     *
     * <p>
     * This operation performs request validation using {@link BindingResult}
     * and persists the card information if all validation rules are satisfied.
     * </p>
     *
     * @param cardDTO       the data transfer object containing card details to be
     *                      saved
     * @param bindingResult holds validation errors, if any
     * @return a {@link ResponseEntity} containing a {@link MessageDTO} that
     *         represents
     *         the result of the card creation operation
     */
    ResponseEntity<MessageDTO> saveNewCard(CardDTO cardDTO, BindingResult bindingResult);

    /**
     * Retrieves all payment cards associated with a specific customer.
     *
     * <p>
     * This method returns a summarized view of cards belonging to the given
     * customer,
     * excluding sensitive card information.
     * </p>
     *
     * @param requestDTO the request object containing the customer identifier
     * @return a {@link ResponseEntity} containing a list of {@link CardSummaryDTO}
     *         representing the customer's cards
     */
    ResponseEntity<List<CardSummaryDTO>> getCardsByCustomer(CustomerIdRequestDTO requestDTO);

    /**
     * Deactivates an existing payment card.
     *
     * <p>
     * This operation marks the specified card as inactive, preventing it from being
     * used in future payment transactions while keeping its record for audit
     * purposes.
     * </p>
     *
     * @param requestDTO the request object containing the card identifier to be
     *                   deactivated
     * @return a {@link ResponseEntity} containing a {@link MessageDTO} that
     *         indicates
     *         the outcome of the deactivation process
     */
    ResponseEntity<MessageDTO> deactivateCard(CardDeactivationRequestDTO requestDTO);

    /**
     * Checks whether a specific payment card is currently active.
     *
     * <p>
     * This method is typically used to verify card usability before initiating
     * a payment transaction.
     * </p>
     *
     * @param requestDTO the request object containing the card identifier
     * @return a {@link ResponseEntity} containing a boolean value indicating
     *         whether the card is active
     */
    ResponseEntity<Boolean> checkCardActive(CardIdRequestDTO requestDTO);

}
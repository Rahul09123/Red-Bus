/**
 * REST controller interface for handling ticket payment operations.
 *
 * <p>
 * This interface defines the contract for processing ticket payments via HTTP
 * endpoints. Implementing classes are responsible for receiving payment requests,
 * delegating business logic to the service layer, and returning standardized
 * payment responses.
 * </p>
 *
 * <p>
 * The controller focuses solely on request–response orchestration and does not
 * contain any payment or transaction logic. All business rules and validations
 * are expected to be handled by the underlying service components.
 * </p>
 *
 * @see TicketPaymentRequestDTO
 * @see TicketPaymentResponseDTO
 * @see org.springframework.web.bind.annotation.RestController
 *
 * @author Ömer Tahsin Taşkın
 *         <a href="https://github.com/omertahsintaskin">https://github.com/omertahsintaskin</a>
 * @version 2.0
 */

package com.shubilet.payment_service.controllers;

import com.shubilet.payment_service.dataTransferObjects.requests.TicketPaymentRequestDTO;
import com.shubilet.payment_service.dataTransferObjects.responses.TicketPaymentResponseDTO;
import org.springframework.http.ResponseEntity;

public interface PaymentController {

    /**
     * Processes a ticket payment request.
     *
     * <p>
     * This method initiates the payment flow for a ticket purchase by validating
     * the provided payment and ticket information and executing the required
     * payment transaction logic. The operation is expected to handle all necessary
     * business validations before producing a payment result.
     * </p>
     *
     * <p>
     * The response contains a {@link TicketPaymentResponseDTO} representing the
     * outcome of the payment operation, including success or failure details and
     * any relevant transaction metadata.
     * </p>
     *
     * @param dto the request payload containing ticket and payment information
     * @return a {@link ResponseEntity} containing {@link TicketPaymentResponseDTO}
     *         with the result of the payment process
     *
     * @see TicketPaymentRequestDTO
     * @see TicketPaymentResponseDTO
     */

    ResponseEntity<TicketPaymentResponseDTO> makePayment(TicketPaymentRequestDTO dto);
}
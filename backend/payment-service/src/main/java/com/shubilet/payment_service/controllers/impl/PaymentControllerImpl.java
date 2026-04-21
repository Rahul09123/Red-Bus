/**
 * REST controller implementation for ticket payment operations.
 *
 * <p>
 * This controller serves as the HTTP entry point for processing ticket payments.
 * It receives payment requests, delegates all business logic to the
 * {@link PaymentService}, and returns standardized HTTP responses to the client.
 * </p>
 *
 * <p>
 * The controller follows a thin-controller design principle, containing no
 * payment logic or validation rules. Any {@link RuntimeException} raised during
 * request processing is handled locally to ensure a consistent error response
 * structure.
 * </p>
 *
 * <p>
 * This implementation fulfills the contract defined by {@link PaymentController}
 * and is designed to be stateless and scalable within a microservice
 * architecture.
 * </p>
 *
 * @see PaymentController
 * @see PaymentService
 * @see org.springframework.web.bind.annotation.RestController
 *
 * @author Ömer Tahsin Taşkın
 *         <a href="https://github.com/omertahsintaskin">https://github.com/omertahsintaskin</a>
 * @version 2.0
 */

package com.shubilet.payment_service.controllers.impl;

import com.shubilet.payment_service.controllers.PaymentController;
import com.shubilet.payment_service.dataTransferObjects.requests.TicketPaymentRequestDTO;
import com.shubilet.payment_service.dataTransferObjects.responses.MessageDTO;
import com.shubilet.payment_service.dataTransferObjects.responses.TicketPaymentResponseDTO;
import com.shubilet.payment_service.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentControllerImpl implements PaymentController {

    private final PaymentService paymentService;

    public PaymentControllerImpl(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
 * Handles an incoming ticket payment request.
 *
 * <p>
 * This method receives the payment request payload and delegates the entire
 * payment processing workflow to the service layer. No business logic or
 * validation is performed at the controller level.
 * </p>
 *
 * <p>
 * If the service layer completes the operation successfully, a
 * {@link TicketPaymentResponseDTO} is returned with HTTP 200 status.
 * Any {@link RuntimeException} thrown during processing is intercepted by the
 * local exception handler to produce a standardized error response.
 * </p>
 *
 * @param dto the request payload containing ticket and payment information
 * @return a {@link ResponseEntity} containing {@link TicketPaymentResponseDTO}
 *         representing the result of the payment operation
 *
 * @see PaymentService#processTicketPayment(TicketPaymentRequestDTO)
 * @see #handleException(RuntimeException)
 */

    @Override
    @PostMapping
    public ResponseEntity<TicketPaymentResponseDTO> makePayment(@RequestBody TicketPaymentRequestDTO dto) {
        // Servis hata fırlatırsa aşağıdaki handler yakalayacak
        TicketPaymentResponseDTO response = paymentService.processTicketPayment(dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Handles runtime exceptions thrown during payment processing.
     *
     * <p>
     * This method intercepts any {@link RuntimeException} that occurs within the
     * controller methods and constructs a standardized error response.
     * </p>
     *
     * @param e the runtime exception that was thrown
     * @return a {@link ResponseEntity} containing a {@link MessageDTO} with error
     *         details
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MessageDTO> handleException(RuntimeException e) {
        return ResponseEntity.badRequest().body(new MessageDTO("Payment Failed: " + e.getMessage()));
    }
}
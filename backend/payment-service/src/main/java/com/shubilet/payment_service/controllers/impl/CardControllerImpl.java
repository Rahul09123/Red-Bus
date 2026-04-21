/**
 * REST controller responsible for managing payment card operations.
 *
 * <p>
 * This controller exposes HTTP endpoints for creating, retrieving, deactivating,
 * and validating customer payment cards. It acts as the entry point for card-related
 * requests and delegates all business logic to the {@link CardService} layer.
 * </p>
 *
 * <p>
 * Input validation is performed using {@code @Valid} and {@link BindingResult}.
 * Any {@link RuntimeException} thrown during request processing is handled locally
 * by a controller-level {@link ExceptionHandler}, ensuring consistent error responses
 * without altering method return types.
 * </p>
 *
 * <p>
 * The controller follows a thin-controller approach, containing no business logic
 * and focusing solely on request handling, validation, and response construction.
 * </p>
 *
 * @see CardController
 * @see CardService
 * @see org.springframework.web.bind.annotation.RestController
 *
 * @author Ömer Tahsin Taşkın
 *         <a href="https://github.com/omertahsintaskin">https://github.com/omertahsintaskin</a>
 * @version 2.0
 */

package com.shubilet.payment_service.controllers.impl;

import com.shubilet.payment_service.controllers.CardController;
import com.shubilet.payment_service.dataTransferObjects.requests.CardDTO;
import com.shubilet.payment_service.dataTransferObjects.requests.CardDeactivationRequestDTO;
import com.shubilet.payment_service.dataTransferObjects.requests.CardIdRequestDTO;
import com.shubilet.payment_service.dataTransferObjects.requests.CustomerIdRequestDTO;
import com.shubilet.payment_service.dataTransferObjects.responses.CardSummaryDTO;
import com.shubilet.payment_service.dataTransferObjects.responses.MessageDTO;
import com.shubilet.payment_service.services.CardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cards")
public class CardControllerImpl implements CardController {

    private final CardService cardService;

    public CardControllerImpl(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * Creates a new payment card after validating the incoming {@link CardDTO}.
     *
     * <p>
     * Uses {@link BindingResult} to detect validation errors produced by
     * {@code @Valid}.
     * If validation fails, a {@link RuntimeException} is thrown and handled by the
     * local
     * {@code @ExceptionHandler} to return a {@link MessageDTO} error response.
     * </p>
     *
     * @param cardDTO       the card payload to be persisted
     * @param bindingResult container for validation errors
     * @return a {@link ResponseEntity} containing a {@link MessageDTO} with a
     *         success message
     *         and the newly created card identifier
     *
     * @see CardService#saveNewCard(CardDTO)
     * @see #handleException(RuntimeException)
     */
    @Override
    @PostMapping("/newcard")
    public ResponseEntity<MessageDTO> saveNewCard(@RequestBody @Valid CardDTO cardDTO, BindingResult bindingResult) {
        // Validasyon Hatası Kontrolü
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            throw new RuntimeException("Validation Error: " + errorMsg);
        }

        CardSummaryDTO summary = cardService.saveNewCard(cardDTO);
        return ResponseEntity.ok(new MessageDTO("Card saved successfully. ID: " + summary.getCardId()));
    }

    /**
     * Returns the list of cards for a given customer.
     *
     * <p>
     * Retrieves card summaries for the provided customer identifier. Exceptions are
     * not
     * caught locally in this method; any {@link RuntimeException} is delegated to
     * the
     * controller-level exception handler.
     * </p>
     *
     * @param requestDTO request payload containing the customer identifier
     * @return a {@link ResponseEntity} containing a list of {@link CardSummaryDTO}
     *
     * @see CardService#getCardsByCustomer(int)
     * @see #handleException(RuntimeException)
     */
    @Override
    @PostMapping("/customer")
    public ResponseEntity<List<CardSummaryDTO>> getCardsByCustomer(@RequestBody CustomerIdRequestDTO requestDTO) {
        // Try-Catch yok, hata olursa @ExceptionHandler yakalayacak
        List<CardSummaryDTO> cards = cardService.getCardsByCustomer(requestDTO.getCustomerId());
        return ResponseEntity.ok(cards);
    }

    /**
     * Deactivates a specific card for the given customer.
     *
     * <p>
     * Delegates the deactivation process to the service layer. If the card does not
     * exist
     * or does not belong to the customer, this method throws a
     * {@link RuntimeException}
     * so that the local exception handler can produce a standardized error
     * response.
     * </p>
     *
     * @param requestDTO request payload containing the card identifier and customer
     *                   identifier
     * @return a {@link ResponseEntity} containing a {@link MessageDTO} indicating
     *         success
     *
     * @see CardService#deactivateCard(int, int)
     * @see #handleException(RuntimeException)
     */
    @Override
    @PostMapping("/deactivate")
    public ResponseEntity<MessageDTO> deactivateCard(@RequestBody CardDeactivationRequestDTO requestDTO) {
        boolean success = cardService.deactivateCard(requestDTO.getCardId(), requestDTO.getCustomerId());

        if (success) {
            return ResponseEntity.ok(new MessageDTO("Card deactivated successfully."));
        } else {
            // RuntimeException fırlatıyoruz ki handler yakalasın
            throw new RuntimeException("Card not found or does not belong to customer.");
        }
    }

    /**
     * Checks whether a specific card is currently active.
     *
     * <p>
     * This method is typically used as a lightweight validation step before
     * initiating
     * payment operations.
     * </p>
     *
     * @param requestDTO request payload containing the card identifier
     * @return a {@link ResponseEntity} containing {@code true} if the card is
     *         active; otherwise {@code false}
     *
     * @see CardService#isCardActive(int)
     * @see #handleException(RuntimeException)
     */
    @Override
    @PostMapping("/check-active")
    public ResponseEntity<Boolean> checkCardActive(@RequestBody CardIdRequestDTO requestDTO) {
        boolean isActive = cardService.isCardActive(requestDTO.getCardId());
        return ResponseEntity.ok(isActive);
    }

    // --- LOCAL EXCEPTION HANDLER ---
    // Bu controller içindeki herhangi bir metot RuntimeException fırlatırsa bu
    // metot çalışır.
    // Böylece ana metotların dönüş tipi bozulmaz (List, Boolean vb. kalabilir).
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MessageDTO> handleException(RuntimeException e) {
        return ResponseEntity.badRequest().body(new MessageDTO(e.getMessage()));
    }
}
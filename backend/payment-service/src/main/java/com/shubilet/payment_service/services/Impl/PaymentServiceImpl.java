package com.shubilet.payment_service.services.Impl;

import com.shubilet.payment_service.common.enums.PaymentStatus;
import com.shubilet.payment_service.dataTransferObjects.requests.TicketPaymentRequestDTO;
import com.shubilet.payment_service.dataTransferObjects.responses.TicketPaymentResponseDTO;
import com.shubilet.payment_service.models.Card;
import com.shubilet.payment_service.models.Payment;
import com.shubilet.payment_service.repositories.CardRepository;
import com.shubilet.payment_service.repositories.PaymentRepository;
import com.shubilet.payment_service.services.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    // Logger Tanımlaması
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final CardRepository cardRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, CardRepository cardRepository) {
        this.paymentRepository = paymentRepository;
        this.cardRepository = cardRepository;
    }

    @Override
    public TicketPaymentResponseDTO processTicketPayment(TicketPaymentRequestDTO requestDTO) {
        logger.info("Processing payment for Customer: {}, Card: {}, Raw Amount: {}", 
                requestDTO.getCustomerId(), requestDTO.getCardId(), requestDTO.getAmount());

        // --- 1. KART VE MÜŞTERİ DOĞRULAMASI ---
        Card card = cardRepository.findById(requestDTO.getCardId())
                .orElseThrow(() -> {
                    logger.error("Payment failed: Card not found ID: {}", requestDTO.getCardId());
                    return new RuntimeException("Card not found with ID: " + requestDTO.getCardId());
                });

        if (!card.getCustomerId().equals(requestDTO.getCustomerId())) {
            logger.warn("Security Alert: Customer {} tried to use Card {} belonging to Customer {}", 
                    requestDTO.getCustomerId(), card.getId(), card.getCustomerId());
            throw new RuntimeException("This card does not belong to the current customer!");
        }

        if (!Boolean.TRUE.equals(card.getIsActive())) {
            logger.warn("Payment failed: Card {} is inactive.", card.getId());
            throw new RuntimeException("Card is not active! Payment cannot be processed.");
        }

        // --- 2. TUTAR KONTROLÜ ---
        if (requestDTO.getAmount() == null || requestDTO.getAmount().trim().isEmpty()) {
            throw new RuntimeException("Payment amount cannot be empty!");
        }

        BigDecimal amount;
        try {
            String cleanAmount = requestDTO.getAmount().replaceAll("[^\\d.,]", "");
            cleanAmount = cleanAmount.replace(",", ".");
            amount = new BigDecimal(cleanAmount);
            
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                logger.error("Invalid payment amount: {}", amount);
                throw new RuntimeException("Payment amount must be greater than zero!");
            }
            
        } catch (NumberFormatException e) {
            logger.error("Amount parsing error for value: {}", requestDTO.getAmount());
            throw new RuntimeException("Invalid amount format! Please enter a valid number (e.g. 150.50)");
        }

        // --- 3. ÖDEME ONAYI VE KAYIT ---
        Payment payment = new Payment();
        payment.setCardId(card.getId());
        payment.setAmount(amount);
        
        Payment saved = paymentRepository.save(payment);
        
        logger.info("Payment SUCCESS. Payment ID: {}, Amount: {}, Ticket Status: PENDING", saved.getId(), saved.getAmount());

        TicketPaymentResponseDTO responseDTO = new TicketPaymentResponseDTO();
        responseDTO.setStatus(PaymentStatus.SUCCESS.name());
        responseDTO.setMessage("Payment completed successfully.");
        responseDTO.setPaymentId(saved.getId());
        responseDTO.setTicketId("TICKET_PENDING");

        return responseDTO;
    }
}
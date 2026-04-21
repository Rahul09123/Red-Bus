package com.shubilet.payment_service.services;

import com.shubilet.payment_service.dataTransferObjects.requests.TicketPaymentRequestDTO;
import com.shubilet.payment_service.dataTransferObjects.responses.TicketPaymentResponseDTO;

public interface PaymentService {

    /**
     * Bilet ödeme işlemini gerçekleştirir.
     * @param requestDTO Ödeme detayları (cardId, amount, customerId)
     * @return İşlem sonucu ve bilet durumu
     */
    TicketPaymentResponseDTO processTicketPayment(TicketPaymentRequestDTO requestDTO);
}
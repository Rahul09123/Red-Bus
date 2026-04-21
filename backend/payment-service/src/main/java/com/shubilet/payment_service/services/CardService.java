package com.shubilet.payment_service.services;

import com.shubilet.payment_service.dataTransferObjects.requests.CardDTO;
import com.shubilet.payment_service.dataTransferObjects.responses.CardSummaryDTO;

import java.util.List;

public interface CardService {

    List<CardSummaryDTO> getCardsByCustomer(int customerId);

    CardSummaryDTO saveNewCard(CardDTO cardDTO);

    boolean deactivateCard(int cardId, int customerId);

    CardSummaryDTO getCardById(int cardId);

    boolean isCardActive(int cardId);
}

package com.shubilet.payment_service.services.Impl;

import com.shubilet.payment_service.dataTransferObjects.requests.CardDTO;
import com.shubilet.payment_service.dataTransferObjects.responses.CardSummaryDTO;
import com.shubilet.payment_service.models.Card;
import com.shubilet.payment_service.repositories.CardRepository;
import com.shubilet.payment_service.services.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CardServiceImpl implements CardService {

    // Logger Tanımlaması
    private static final Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);

    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public CardSummaryDTO saveNewCard(CardDTO cardDTO) {
        logger.info("Request received to save new card for Customer ID: {}", cardDTO.getCustomerId());

        // --- 1. VALIDATION: TEMİZLİK ---
        String rawNumber = cardDTO.getCardNumber() != null ? cardDTO.getCardNumber().replaceAll("\\D", "") : "";
        
        // --- 2. VALIDATION: DUPLICATE KONTROLÜ ---
        if (cardRepository.existsByCardNoAndCustomerIdAndIsActiveTrue(rawNumber, cardDTO.getCustomerId())) {
            logger.warn("Duplicate card attempt! Customer {} tried to add an existing card.", cardDTO.getCustomerId());
            throw new RuntimeException("You have already added this card to your account!");
        }

        // --- 3. VALIDATION: TARİH KONTROLÜ ---
        String monthStr = cardDTO.getExpirationMonth();
        String yearStr = cardDTO.getExpirationYear();
        
        int month = Integer.parseInt(monthStr);
        int year = Integer.parseInt(yearStr) + 2000;

        if (month < 1 || month > 12) {
            logger.error("Invalid month provided: {}", month);
            throw new RuntimeException("Invalid expiration month: " + month);
        }

        YearMonth cardExpiry = YearMonth.of(year, month);
        YearMonth now = YearMonth.now();

        if (cardExpiry.isBefore(now)) {
            logger.warn("Expired card attempt by Customer {}. Expiry: {}/{}", cardDTO.getCustomerId(), month, year);
            throw new RuntimeException("Card is expired! Check expiration date.");
        }

        // --- ENTITY OLUŞTURMA ---
        Card card = new Card();
        card.setCustomerId(cardDTO.getCustomerId());
        card.setCardNo(rawNumber);

        // İsim Soyisim
        String holder = cardDTO.getCardHolderName();
        if (holder != null) {
            String normalized = holder.trim().replaceAll("\\s+", " ");
            String[] parts = normalized.split(" ", 2);
            card.setName(parts[0]);
            card.setSurname(parts.length > 1 ? parts[1] : "-");
        } else {
            card.setName("-");
            card.setSurname("-");
        }

        // Tarih
        if (monthStr.length() == 1) monthStr = "0" + monthStr;
        card.setExpirationDate(monthStr + "/" + yearStr);

        // CVC
        String cvc = cardDTO.getCvc() != null ? cardDTO.getCvc().replaceAll("\\D", "") : "";
        card.setCvc(cvc);

        card.setIsActive(true);

        Card saved = cardRepository.save(card);
        
        logger.info("Card saved successfully. Card ID: {}, Customer ID: {}", saved.getId(), saved.getCustomerId());

        return convertToSummaryDTO(saved);
    }

    @Override
    public boolean deactivateCard(int cardId, int customerId) {
        logger.info("Deactivation request for Card ID: {}, Customer ID: {}", cardId, customerId);

        Optional<Card> optionalCard = cardRepository.findById(cardId);
        
        if (optionalCard.isEmpty()) {
            logger.error("Card not found with ID: {}", cardId);
            throw new RuntimeException("Card not found with ID: " + cardId);
        }

        Card card = optionalCard.get();

        if (!Integer.valueOf(customerId).equals(card.getCustomerId())) {
             logger.warn("Unauthorized deactivation attempt! User {} tried to delete Card {}", customerId, cardId);
             throw new RuntimeException("This card does not belong to the specified customer!");
        }

        if (!Boolean.TRUE.equals(card.getIsActive())) {
             logger.info("Card {} is already deactivated.", cardId);
             throw new RuntimeException("Card is already deactivated!");
        }

        card.setIsActive(false);
        cardRepository.save(card);

        logger.info("Card {} deactivated successfully.", cardId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardSummaryDTO> getCardsByCustomer(int customerId) {
        logger.debug("Fetching cards for Customer ID: {}", customerId); // Debug seviyesi, bilgi kirliliği yapmasın diye
        
        if (customerId <= 0) {
            throw new RuntimeException("Customer ID must be greater than 0");
        }
        List<CardSummaryDTO> cards = cardRepository.findByCustomerIdAndIsActiveTrue(customerId)
                .stream()
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());
        
        logger.info("Found {} active cards for Customer ID: {}", cards.size(), customerId);
        return cards;
    }

    @Override
    @Transactional(readOnly = true)
    public CardSummaryDTO getCardById(int cardId) {
        return cardRepository.findById(cardId)
                .map(this::convertToSummaryDTO)
                .orElse(null);
    }

    @Override
    public boolean isCardActive(int cardId) {
        Optional<Card> cardOpt = cardRepository.findById(cardId);
        if (cardOpt.isEmpty()) {
            logger.warn("Status check failed: Card not found ID: {}", cardId);
            throw new RuntimeException("Card not found for status check!");
        }
        boolean isActive = Boolean.TRUE.equals(cardOpt.get().getIsActive());
        logger.debug("Card ID: {} Active Status: {}", cardId, isActive);
        return isActive;
    }

    private CardSummaryDTO convertToSummaryDTO(Card card) {
        String cardNo = card.getCardNo() != null ? card.getCardNo() : "";
        String last4 = cardNo.length() >= 4 ? cardNo.substring(cardNo.length() - 4) : cardNo;

        String exp = card.getExpirationDate() != null ? card.getExpirationDate() : "";
        String expMonth = "";
        String expYear = "";
        if (exp.contains("/")) {
            String[] parts = exp.split("/", 2);
            expMonth = parts[0];
            expYear = parts.length > 1 ? parts[1] : "";
        }

        return new CardSummaryDTO(String.valueOf(card.getId()), last4, expMonth, expYear);
    }
}
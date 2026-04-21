package com.shubilet.expedition_service.common.util;

import org.springframework.http.ResponseEntity;

import com.shubilet.expedition_service.common.constants.ErrorMessages;
import com.shubilet.expedition_service.dataTransferObjects.responses.complex.CardsDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.complex.ExpeditionsForCompanyDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.complex.ExpeditionsForCustomerDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.complex.SeatsForCompanyDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.complex.SeatsForCustomerDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.complex.TicketsDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.message.MessageDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.middle.ExpeditionInfoForCompanyDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.middle.TicketInfoDTO;

/****

    Domain: ErrorHandling

    Provides a centralized utility for constructing standardized error responses across the application.
    This class maps business and validation error scenarios to appropriate HTTP status codes and converts
    them into typed DTO-based {@link ResponseEntity} responses according to the configured {@link ConversionType}.
    It enables consistent error messaging and payload structures for different API domains such as expeditions,
    tickets, cards, and sessions, while abstracting repetitive response-building logic from controllers.

    <p>

        Technologies:

        <ul>
            <li>Spring Web</li>
            <li>Core Java</li>
        </ul>

    </p>

    @author Abdullah (Mirliva) GÜNDÜZ - https://github.com/MrMilriva

    @version 3.0
*/
public class ErrorUtils {
    private ConversionType conversionType;

    public ErrorUtils(ConversionType conversionType) {
        this.conversionType = conversionType;
    }

    public <T> ResponseEntity<T> isNull(String fieldName) {
        String message = fieldName + ErrorMessages.NULL_OR_EMPTY;
        return caster(message, 400);
    }

    public <T> ResponseEntity<T> isInvalidFormat(String fieldName) {
        String message = fieldName + ErrorMessages.INVALID_FORMAT;
        return caster(message, 400);
    }

    public <T> ResponseEntity<T> criticalError() {
        String message = ErrorMessages.CRITICAL_ERROR;
        return caster(message, 500);
    }

    public <T> ResponseEntity<T> notFound(String fieldName) {
        String message = fieldName + ErrorMessages.NOT_FOUND;
        return caster(message, 400);
    }

    public <T> ResponseEntity<T> unauthorized() {
        String message = ErrorMessages.SESSION_NOT_FOUND;
        return caster(message, 401);
    }

    public <T> ResponseEntity<T> sameCityError() {
        String message = ErrorMessages.SAME_CITY_ERROR_MESSAGE;
        return caster(message, 400);
    }

    public <T> ResponseEntity<T> alreadyExists(String entityName) {
        String message = entityName + ErrorMessages.ALREADY_EXISTS;
        return caster(message, 409);
    }

    public <T> ResponseEntity<T> alreadyBooked(String entityName) {
        String message = entityName + ErrorMessages.ALREADY_BOOKED;
        return caster(message, 409);
    }

    public <T> ResponseEntity<T> dateInPastError() {
        String message = ErrorMessages.DATE_IN_PAST_ERROR;
        return caster(message, 400);
    }

    public <T> ResponseEntity<T> cardNotActive() {
        String message = ErrorMessages.CARD_NOT_ACTIVE;
        return caster(message, 400);
    }

    public <T> ResponseEntity<T> customError(ResponseEntity<?> responseEntity, String message) {
        int statusCode = responseEntity.getStatusCode().value();
        return caster(message, statusCode);
    }

    private <T> ResponseEntity<T> caster(String errorMessage, int errorCode) {
        Object errorObj = null;

        switch (conversionType) {
            case MESSAGE_DTO:
                errorObj = new MessageDTO(errorMessage);
                break;
            case EXPEDITION_INFO_FOR_COMPANY_DTO:
                errorObj = new ExpeditionInfoForCompanyDTO(errorMessage);
                break;
            case TICKET_INFO_DTO:
                errorObj = new TicketInfoDTO(errorMessage);
                break;
            case CARDS_DTO:
                errorObj = new CardsDTO(errorMessage);
                break;
            case EXPEDITIONS_FOR_COMPANY_DTO:
                errorObj = new ExpeditionsForCompanyDTO(errorMessage);
                break;
            case EXPEDITIONS_FOR_CUSTOMER_DTO:
                errorObj = new ExpeditionsForCustomerDTO(errorMessage);
                break;
            case SEATS_FOR_CUSTOMER_DTO:
                errorObj = new SeatsForCustomerDTO(errorMessage);
                break;
            case SEATS_FOR_COMPANY_DTO:
                errorObj = new SeatsForCompanyDTO(errorMessage);
                break;
            case TICKETS_DTO:
                errorObj = new TicketsDTO(errorMessage);
                break;
            default:
                throw new IllegalArgumentException("Unsupported conversion type");
        }

        return ResponseEntity.status(errorCode).body((T) errorObj);
    }

    public enum ConversionType {
        MESSAGE_DTO,
        EXPEDITION_INFO_FOR_COMPANY_DTO,
        TICKET_INFO_DTO,
        CARDS_DTO,
        EXPEDITIONS_FOR_COMPANY_DTO,
        EXPEDITIONS_FOR_CUSTOMER_DTO,
        SEATS_FOR_CUSTOMER_DTO,
        SEATS_FOR_COMPANY_DTO,
        TICKETS_DTO
    }

    // Mirliva says: Bug varsa kaderdir.
    // Yoksa log eksiktir.
}

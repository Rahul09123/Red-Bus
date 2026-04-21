package com.shubilet.member_service.common.util;

import com.shubilet.member_service.common.constants.ErrorMessages;
import com.shubilet.member_service.dataTransferObjects.requests.profile.CustomerProfileDTO;
import com.shubilet.member_service.dataTransferObjects.responses.CompanyIdNameMapDTO;
import com.shubilet.member_service.dataTransferObjects.responses.CustomerIdNameMapDTO;
import com.shubilet.member_service.dataTransferObjects.responses.MemberSessionDTO;
import com.shubilet.member_service.dataTransferObjects.responses.MessageDTO;
import com.shubilet.member_service.dataTransferObjects.responses.UnverifiedAdminsDTO;
import com.shubilet.member_service.dataTransferObjects.responses.UnverifiedCompaniesDTO;

import org.springframework.http.ResponseEntity;

public final class ErrorUtils {
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

    public <T> ResponseEntity<T> alreadyExists(String fieldName) {
        String message = fieldName + ErrorMessages.ALREADY_EXISTS;
        return caster(message, 400);
    }

    public <T> ResponseEntity<T> unauthorized(String message) {
        return caster(message, 401);
    }


    public <T> ResponseEntity<T> customError(ResponseEntity<?> responseEntity, String message) {
        int statusCode = responseEntity.getStatusCode().value();
        return caster(message, statusCode);
    }

    private <T> ResponseEntity<T> caster(String errorMessage, int errorCode) {
        Object errorObj = switch (conversionType) {
            case UnVerifiedAdminsDTO -> new UnverifiedAdminsDTO(errorMessage);
            case UnVerifiedCompaniesDTO -> new UnverifiedCompaniesDTO(errorMessage);
            case CompanyIdNameMapDTO -> new CompanyIdNameMapDTO(errorMessage);
            case MemberSessionDTO -> new MemberSessionDTO(errorMessage);
            case CustomerIdNameMapDTO -> new CustomerIdNameMapDTO(errorMessage);
            case CustomerProfileDTO -> new CustomerProfileDTO(errorMessage);
            case MessageDTO -> new MessageDTO(errorMessage);

        };

        return ResponseEntity.status(errorCode).body((T) errorObj);
    }

    public enum ConversionType {
        UnVerifiedAdminsDTO,
        UnVerifiedCompaniesDTO,
        CompanyIdNameMapDTO,
        MemberSessionDTO,
        CustomerIdNameMapDTO,
        MessageDTO, CustomerProfileDTO,

    }
}

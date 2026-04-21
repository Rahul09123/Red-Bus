package com.shubilet.api_gateway.mappers.profileManagement;

import com.shubilet.api_gateway.dataTransferObjects.external.requests.profileManagement.CardCreationExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.profileManagement.CardCreationInternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.auth.MemberCheckMessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardCreationExternalMapper {


    @Mapping(source = "cardCreationDTO.cardHolderName", target = "cardHolderName")
    @Mapping(source = "cardCreationDTO.cardNumber", target = "cardNumber")
    @Mapping(source = "cardCreationDTO.expirationMonth", target = "expirationMonth")
    @Mapping(source = "cardCreationDTO.expirationYear", target = "expirationYear")
    @Mapping(source = "cardCreationDTO.cvc", target = "cvc")
    @Mapping(source = "memberCheckMessageDTO.userId", target = "customerId")
    CardCreationInternalDTO toCardCreationInternalDTO(CardCreationExternalDTO cardCreationDTO, MemberCheckMessageDTO memberCheckMessageDTO);
}

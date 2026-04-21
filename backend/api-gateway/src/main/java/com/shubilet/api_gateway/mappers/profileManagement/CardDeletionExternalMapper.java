package com.shubilet.api_gateway.mappers.profileManagement;

import com.shubilet.api_gateway.dataTransferObjects.external.requests.profileManagement.CardDeletionExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.profileManagement.CardDeletionInternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.auth.MemberCheckMessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardDeletionExternalMapper {

    @Mapping(source = "cardDeletionExternalDTO.cardId", target = "cardId")
    @Mapping(source = "memberCheckMessageDTO.userId", target = "customerId")
    CardDeletionInternalDTO toCardDeletionInternalDTO(CardDeletionExternalDTO cardDeletionExternalDTO, MemberCheckMessageDTO memberCheckMessageDTO);

}

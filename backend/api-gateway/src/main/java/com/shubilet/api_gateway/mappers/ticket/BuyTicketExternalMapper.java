package com.shubilet.api_gateway.mappers.ticket;

import com.shubilet.api_gateway.dataTransferObjects.external.requests.expeditionOperations.BuyTicketExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.ticket.BuyTicketInternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.auth.MemberCheckMessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BuyTicketExternalMapper {

    @Mapping(source = "buyTicketExternalDTO.expeditionId", target = "expeditionId")
    @Mapping(source = "buyTicketExternalDTO.seatNo", target = "seatNo")
    @Mapping(source = "buyTicketExternalDTO.cardId", target = "cardId")
    @Mapping(source = "memberCheckMessageDTO.userId", target = "customerId")
    BuyTicketInternalDTO toBuyTicketInternalDTO(BuyTicketExternalDTO buyTicketExternalDTO, MemberCheckMessageDTO memberCheckMessageDTO);
}

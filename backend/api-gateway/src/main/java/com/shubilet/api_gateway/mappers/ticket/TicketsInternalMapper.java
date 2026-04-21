package com.shubilet.api_gateway.mappers.ticket;

import com.shubilet.api_gateway.dataTransferObjects.internal.requests.CompanyIdDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.ticket.TicketInternalDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketsInternalMapper {

    @Mapping(source = "t.companyId", target = "companyId")
    CompanyIdDTO toCompanyIdDTO(TicketInternalDTO t);

    List<CompanyIdDTO> toCompanyIdDTOs(List<TicketInternalDTO> ticketInternalDTOS);
}

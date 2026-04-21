package com.shubilet.api_gateway.mappers.expeditionOperations;

import com.shubilet.api_gateway.dataTransferObjects.external.requests.expeditionOperations.ExpeditionCreationExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.expeditionOperations.ExpeditionCreationInternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.auth.MemberCheckMessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpeditionCreationExternalMapper {

    @Mapping(source = "memberCheckMessageDTO.userId", target = "companyId")
    @Mapping(source = "expeditionCreationExternalDTO.departureCity", target = "departureCity")
    @Mapping(source = "expeditionCreationExternalDTO.arrivalCity", target = "arrivalCity")
    @Mapping(source = "expeditionCreationExternalDTO.date", target = "date")
    @Mapping(source = "expeditionCreationExternalDTO.time", target = "time")
    @Mapping(source = "expeditionCreationExternalDTO.price", target = "price")
    @Mapping(source = "expeditionCreationExternalDTO.duration", target = "duration")
    @Mapping(source = "expeditionCreationExternalDTO.capacity", target = "capacity")
    ExpeditionCreationInternalDTO toExpeditionCreationInternalDTO(ExpeditionCreationExternalDTO expeditionCreationExternalDTO, MemberCheckMessageDTO memberCheckMessageDTO);
}

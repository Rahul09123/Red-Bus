package com.shubilet.api_gateway.mappers.expeditionOperations;

import com.shubilet.api_gateway.dataTransferObjects.external.requests.expeditionOperations.ExpeditionIdDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.expeditionOperations.ExpeditionViewForCompanyByIdInternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.auth.MemberCheckMessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpeditionIdMapper {

    @Mapping(source = "expeditionIdDTO.expeditionId", target = "expeditionId")
    @Mapping(source = "memberCheckMessageDTO.userId", target = "companyId")
    ExpeditionViewForCompanyByIdInternalDTO toExpeditionViewForCompanyByIdInternalDTO(ExpeditionIdDTO expeditionIdDTO, MemberCheckMessageDTO memberCheckMessageDTO);

}

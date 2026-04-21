package com.shubilet.api_gateway.mappers.expeditionOperations;

import com.shubilet.api_gateway.dataTransferObjects.internal.responses.expeditionOperations.ExpeditionForCustomerDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.CompanyIdDTO;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExpeditionSearchCompanyResponseMapper {

    @Mapping(source = "e.companyId", target = "companyId")
    CompanyIdDTO toCompanyIdDTO(ExpeditionForCustomerDTO e);

    List<CompanyIdDTO> toCompanyIdDTOs(List<ExpeditionForCustomerDTO> expeditionForCustomerDTOs);
}

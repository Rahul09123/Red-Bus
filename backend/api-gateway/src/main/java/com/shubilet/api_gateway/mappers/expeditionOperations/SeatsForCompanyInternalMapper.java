package com.shubilet.api_gateway.mappers.expeditionOperations;

import com.shubilet.api_gateway.dataTransferObjects.internal.requests.CustomerIdDTO;


import com.shubilet.api_gateway.dataTransferObjects.internal.responses.expeditionOperations.SeatForCompanyInternalDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SeatsForCompanyInternalMapper {

    @Mapping(source = "s.customerId", target = "customerId")
    CustomerIdDTO toCustomerIdDTO(SeatForCompanyInternalDTO s);

    List<CustomerIdDTO> toCustomerIdDTOs(List<SeatForCompanyInternalDTO> seatForCompanyInternalDTOs);


}

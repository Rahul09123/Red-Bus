package com.shubilet.api_gateway.mappers;


import com.shubilet.api_gateway.dataTransferObjects.internal.CookieDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.AdminIdDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.CompanyIdDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.CustomerIdDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CookieMapper {

    @Mapping(source = "cookieDTO.userId", target = "adminId")
    AdminIdDTO toAdminIdDTO(CookieDTO cookieDTO);

    @Mapping(source = "cookieDTO.userId", target = "companyId")
    CompanyIdDTO toCompanyIdDTO(CookieDTO cookieDTO);

    @Mapping(source = "cookieDTO.userId", target = "customerId")
    CustomerIdDTO toCustomerIdDTO(CookieDTO cookieDTO);
}

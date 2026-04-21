package com.shubilet.api_gateway.mappers.verification;

import com.shubilet.api_gateway.dataTransferObjects.internal.requests.verification.CompanyVerificationInternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.requests.VerificationController.CompanyVerificationExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.auth.MemberCheckMessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CompanyVerificationExternalMapper {

    @Mapping(source = "companyVerificationExternalDTO.companyId", target = "candidateCompanyId")
    @Mapping(source = "memberCheckMessageDTO.userId", target = "adminId")
    CompanyVerificationInternalDTO toCompanyVerificationInternalDTO(CompanyVerificationExternalDTO companyVerificationExternalDTO, MemberCheckMessageDTO memberCheckMessageDTO);
}

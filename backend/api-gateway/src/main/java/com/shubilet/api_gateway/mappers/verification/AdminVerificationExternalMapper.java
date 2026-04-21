package com.shubilet.api_gateway.mappers.verification;

import com.shubilet.api_gateway.dataTransferObjects.external.requests.VerificationController.AdminVerificationExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.verification.AdminVerificationInternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.auth.MemberCheckMessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminVerificationExternalMapper {

    @Mapping(source = "adminVerificationExternalDTO.candidateAdminId", target = "candidateAdminId")
    @Mapping(source = "memberCheckMessageDTO.userId", target = "adminId")
    AdminVerificationInternalDTO toAdminVerificationInternalDTO(AdminVerificationExternalDTO adminVerificationExternalDTO, MemberCheckMessageDTO memberCheckMessageDTO);
}

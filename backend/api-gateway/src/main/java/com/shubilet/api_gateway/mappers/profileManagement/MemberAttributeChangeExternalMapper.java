package com.shubilet.api_gateway.mappers.profileManagement;

import com.shubilet.api_gateway.dataTransferObjects.external.requests.profileManagement.MemberAttributeChangeExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.profileManagement.MemberAttributeChangeInternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.auth.MemberCheckMessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberAttributeChangeExternalMapper {

    @Mapping(source = "memberAttributeChangeExternalDTO.attribute", target = "attribute")
    @Mapping(source = "memberCheckMessageDTO.userId", target = "memberId")
    MemberAttributeChangeInternalDTO toMemberAttributeChangeInternalDTO(MemberAttributeChangeExternalDTO memberAttributeChangeExternalDTO, MemberCheckMessageDTO memberCheckMessageDTO);
}

package com.shubilet.api_gateway.mappers.auth;

import com.shubilet.api_gateway.dataTransferObjects.internal.CookieDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.auth.SessionCreationDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.auth.MemberSessionInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface MemberSessionMapper {

    @Mapping(source = "cookieDTO", target = "cookie")
    @Mapping(source = "memberSessionInfoDTO.userId", target = "userId")
    @Mapping(source = "memberSessionInfoDTO.userType", target = "userType")
    SessionCreationDTO toSessionCreationDTO(CookieDTO cookieDTO, MemberSessionInfoDTO memberSessionInfoDTO);
}

package com.shubilet.api_gateway.mappers.profileManagement;

import com.shubilet.api_gateway.dataTransferObjects.external.requests.profileManagement.FavoriteCompanyAdditionExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.profileManagement.FavoriteCompanyAdditionInternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.auth.MemberCheckMessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FavoriteCompanyAdditionExternalMapper {

    @Mapping(source = "favoriteCompanyAdditionExternalDTO.companyId", target = "companyId")
    @Mapping(source = "memberCheckMessageDTO.userId", target = "customerId")
    FavoriteCompanyAdditionInternalDTO toFavoriteCompanyAdditionInternalDTO(FavoriteCompanyAdditionExternalDTO favoriteCompanyAdditionExternalDTO, MemberCheckMessageDTO memberCheckMessageDTO);

}

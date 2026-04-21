package com.shubilet.api_gateway.mappers.profileManagement;

import com.shubilet.api_gateway.dataTransferObjects.external.requests.profileManagement.FavoriteCompanyDeletionExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.profileManagement.FavoriteCompanyDeletionInternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.auth.MemberCheckMessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FavoriteCompanyDeletionExternalMapper {

    @Mapping(source = "favoriteCompanyDeletionExternalDTO.relationId", target = "relationId")
    @Mapping(source = "memberCheckMessageDTO.userId", target = "customerId")
    FavoriteCompanyDeletionInternalDTO toFavoriteCompanyDeletionInternalDTO(FavoriteCompanyDeletionExternalDTO favoriteCompanyDeletionExternalDTO, MemberCheckMessageDTO memberCheckMessageDTO);

}

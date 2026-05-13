package com.shubilet.api_gateway.mappers.expeditionOperations;

import com.shubilet.api_gateway.dataTransferObjects.external.requests.expeditionOperations.BlockSeatExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.requests.expeditionOperations.BlockSeatInternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.auth.MemberCheckMessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BlockSeatExternalMapper {

    @Mapping(source = "blockSeatExternalDTO.expeditionId", target = "expeditionId")
    @Mapping(source = "blockSeatExternalDTO.seatNo", target = "seatNo")
    @Mapping(source = "memberCheckMessageDTO.userId", target = "customerId")
    BlockSeatInternalDTO toBlockSeatInternalDTO(BlockSeatExternalDTO blockSeatExternalDTO, MemberCheckMessageDTO memberCheckMessageDTO);
}

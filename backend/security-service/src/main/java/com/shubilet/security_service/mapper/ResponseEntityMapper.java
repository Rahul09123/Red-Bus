package com.shubilet.security_service.mapper;

import org.springframework.http.ResponseEntity;

import com.shubilet.security_service.dataTransferObjects.responses.CheckMessageDTO;
import com.shubilet.security_service.dataTransferObjects.responses.MessageDTO;

public class ResponseEntityMapper {
    private ResponseEntityMapper() {
        throw new IllegalStateException("Utility class" );
    }

    public static ResponseEntity<CheckMessageDTO> toCheckMessageDTOResponseEntity(ResponseEntity<MessageDTO> responseEntity) {
        ResponseEntity<MessageDTO> messageDTOResponse = (ResponseEntity<MessageDTO>) responseEntity;
        return messageDTOResponseEntitytoCheckMessageDTOResponseEntity(messageDTOResponse);
    }


    // 
    // HELPER METHODS
    // --------------------------------------------------
    private static ResponseEntity<CheckMessageDTO> messageDTOResponseEntitytoCheckMessageDTOResponseEntity(ResponseEntity<MessageDTO> responseEntity) {
        MessageDTO body = responseEntity.getBody();
        if (body != null) {
            CheckMessageDTO dto = new CheckMessageDTO(body.getCookie(), body.getMessage(), -1);
            return new ResponseEntity<>(dto, responseEntity.getStatusCode());
        }
        return new ResponseEntity<>(null, responseEntity.getStatusCode());
    }
}

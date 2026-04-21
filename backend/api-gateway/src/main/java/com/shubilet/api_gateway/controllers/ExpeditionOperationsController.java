package com.shubilet.api_gateway.controllers;

import com.shubilet.api_gateway.dataTransferObjects.MessageDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.requests.expeditionOperations.ExpeditionCreationExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.requests.expeditionOperations.ExpeditionIdDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.requests.expeditionOperations.ExpeditionSearchDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.responses.expeditionOperations.ExpeditionSearchResultsCompanyDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.responses.expeditionOperations.ExpeditionsForCompanyDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.responses.expeditionOperations.SeatsForCustomerDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface ExpeditionOperationsController {
    @PostMapping("/search")
    ResponseEntity<ExpeditionSearchResultsCompanyDTO> sendExpeditions(HttpSession httpSession, @RequestBody ExpeditionSearchDTO expeditionSearchDTO);

    @PostMapping("/create")
    ResponseEntity<MessageDTO> createExpedition(HttpSession httpSession, ExpeditionCreationExternalDTO expeditionCreationExternalDTO);

    ResponseEntity<SeatsForCustomerDTO> sendSeats(HttpSession httpSession, @RequestBody ExpeditionIdDTO expeditionIdDTO);

    @PostMapping("/company/get/all")
    ResponseEntity<ExpeditionsForCompanyDTO> sendCompanyExpeditions(HttpSession httpSession);

    @PostMapping("/company/get/future")
    ResponseEntity<ExpeditionsForCompanyDTO> sendCompanyFutureExpeditions(HttpSession httpSession);
}

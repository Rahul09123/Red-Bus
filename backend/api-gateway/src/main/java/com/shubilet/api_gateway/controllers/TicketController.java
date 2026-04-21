package com.shubilet.api_gateway.controllers;

import com.shubilet.api_gateway.dataTransferObjects.external.requests.expeditionOperations.BuyTicketExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.external.responses.ticket.TicketsExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.ticket.TicketInfoDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

public interface TicketController {
    public ResponseEntity<TicketsExternalDTO> sendTicketDetailsForCustomer(HttpSession httpSession);

    @PostMapping("/buy")
    ResponseEntity<TicketInfoDTO> buyTicketForCustomer(HttpSession httpSession, BuyTicketExternalDTO buyTicketExternalDTO);
}

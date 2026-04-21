package com.shubilet.expedition_service.services.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shubilet.expedition_service.common.util.DTOMapperUtils;
import com.shubilet.expedition_service.common.util.PNRGenerator;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.TicketDTO;
import com.shubilet.expedition_service.models.Ticket;
import com.shubilet.expedition_service.repositories.TicketRepository;
import com.shubilet.expedition_service.services.TicketService;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(
        TicketRepository ticketRepository
    ) {
        this.ticketRepository = ticketRepository;
    }

    public TicketDTO getTicketDetails(String ticketPNR) {
        return DTOMapperUtils.toTicketDTO(
            ticketRepository.findTicketDetailsByPNR(
                ticketPNR
            )
        );
    }

    public String generateTicket(int paymentId, int seatId, int customerId) {
        String PNR = "";
        
        do {
            PNR = PNRGenerator.generatePNR();
        } while(ticketRepository.existsByPNR(PNR));
        
        Ticket ticket = new Ticket(
            PNR,
            seatId,
            paymentId,
            customerId
        );
        
        ticketRepository.save(ticket);
        return PNR;

        // Mirliva says: Money is temporary.
        // Tickets are forever.
        // (until refund)
    }

    public List<TicketDTO> getTicketsByCustomerId(int customerId) {
        return DTOMapperUtils.toTicketDTO(
            ticketRepository.findTicketsByCustomerId(customerId)
        );
    }
}

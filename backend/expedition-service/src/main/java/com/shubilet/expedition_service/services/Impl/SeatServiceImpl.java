package com.shubilet.expedition_service.services.Impl;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shubilet.expedition_service.common.enums.forReservation.SeatStatus;
import com.shubilet.expedition_service.common.util.DTOMapperUtils;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.SeatForCompanyDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.SeatForCustomerDTO;
import com.shubilet.expedition_service.models.Seat;
import com.shubilet.expedition_service.services.SeatService;
import com.shubilet.expedition_service.repositories.ExpeditionRepository;
import com.shubilet.expedition_service.repositories.SeatRepository;

@Service
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final ExpeditionRepository expeditionRepository;

    public SeatServiceImpl(
        SeatRepository seatRepository,
        ExpeditionRepository expeditionRepository
    ) {
        this.seatRepository = seatRepository;
        this.expeditionRepository = expeditionRepository;
    }
    
    public void generateSeats(int expeditionId, int capacity) {
        for(int i = 0; i < capacity; i++) {
            // Mirliva says: Seats are born equal.
            // But some get booked faster.
            Seat seat = new Seat(expeditionId, i + 1);
            seatRepository.save(seat);
        }
    }

    public List<SeatForCustomerDTO> getByAvailableSeats(int expeditionId) {
        Instant now = Instant.now();
        return DTOMapperUtils.toSeatForCustomerDTO(seatRepository.findSeatsByExpeditionIdAndStatus(expeditionId, now));
    }

    public List<SeatForCompanyDTO> getSeatsByExpeditionIdAndCompanyId(int expeditionId, int companyId) {
        return DTOMapperUtils.toSeatForCompanyDTO(
            seatRepository.findSeatsByExpeditionIdAndCompanyId(
                expeditionId,
                companyId
            )
        );
    }

    public boolean seatExist(int expeditionId, int seatNo) {
        return seatRepository.existsByExpeditionIdAndSeatNo(expeditionId, seatNo);
    }

    public int bookSeat(int expeditionId, int customerId, int seatNo) {
        Instant now = Instant.now();

        Seat seat = seatRepository.findByExpeditionIdAndSeatNo(expeditionId, seatNo);

        if(seat == null) {
            return -1;
        }

        if(expeditionRepository.isExpeditionTimePassed(expeditionId, now)) {
            return -1;
        }

        if(seat.isBooked()) {
            return -1;
        }

        seat.setBooked(true);
        seat.setCustomerId(customerId);
        seatRepository.save(seat);
        
        return seat.getId();
    }

    public SeatStatus canBeReserved(int expeditionId, int seatNo) {
        Seat seat = seatRepository.findByExpeditionIdAndSeatNo(expeditionId, seatNo);

        if(seat == null) {
            return SeatStatus.NOT_FOUND;
        }

        if(seat.isBooked()) {
            return SeatStatus.ALREADY_BOOKED;
        }

        return SeatStatus.SUCCESS;
    }
}
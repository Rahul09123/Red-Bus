package com.shubilet.expedition_service.services;

import java.util.List;

import com.shubilet.expedition_service.common.enums.forReservation.SeatStatus;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.SeatForCompanyDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.SeatForCustomerDTO;

public interface SeatService {
    
    public void generateSeats(int expeditionId, int capacity);

    public List<SeatForCustomerDTO> getByAvailableSeats(int expeditionId);

    public List<SeatForCompanyDTO> getSeatsByExpeditionIdAndCompanyId(int expeditionId, int companyId);

    public boolean seatExist(int expeditionId, int seatNo);

    public int bookSeat(int expeditionId, int customerId, int seatNo);

    public SeatStatus canBeReserved(int expeditionId, int seatNo);
}

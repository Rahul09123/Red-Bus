package com.shubilet.expedition_service.services;

import java.util.List;

import com.shubilet.expedition_service.common.enums.forReservation.ExpeditionStatus;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.ExpeditionForCompanyDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.ExpeditionForCustomerDTO;

public interface ExpeditionService {
    
    public int createExpedition(int companyId, String departureCity, String arrivalCity, String date, String time, int capacity, double price, int duration);

    public List<ExpeditionForCustomerDTO> findExpeditionsByInstantAndRoute(String departureCity, String arrivalCity, String date);

    public List<ExpeditionForCompanyDTO> findExpeditionsByInstantAndCompanyId(String date, int companyId);

    public List<ExpeditionForCompanyDTO> findUpcomingExpeditions(int companyId);

    public List<ExpeditionForCompanyDTO> findAllExpeditions(int companyId);

    public boolean expeditionExists(int expeditionId);

    public boolean bookSeat(int expeditionId);

    public ExpeditionStatus canBeReserved(int expeditionId);

    public int getExpeditionPrice(int expeditionId);
}

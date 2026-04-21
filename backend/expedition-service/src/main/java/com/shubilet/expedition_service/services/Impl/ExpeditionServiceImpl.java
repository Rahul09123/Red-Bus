package com.shubilet.expedition_service.services.Impl;

import java.time.Instant;
import java.util.List;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.shubilet.expedition_service.common.enums.forReservation.ExpeditionStatus;
import com.shubilet.expedition_service.common.util.DTOMapperUtils;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.ExpeditionForCompanyDTO;
import com.shubilet.expedition_service.dataTransferObjects.responses.base.ExpeditionForCustomerDTO;
import com.shubilet.expedition_service.models.Expedition;
import com.shubilet.expedition_service.repositories.CityRepository;
import com.shubilet.expedition_service.repositories.ExpeditionRepository;
import com.shubilet.expedition_service.services.ExpeditionService;

@Service
public class ExpeditionServiceImpl implements ExpeditionService {

    private final ExpeditionRepository expeditionRepository;
    private final CityRepository cityRepository;

    public ExpeditionServiceImpl(
        ExpeditionRepository expeditionRepository,
        CityRepository cityRepository
    ) {
        this.expeditionRepository = expeditionRepository;
        this.cityRepository = cityRepository;
    }

    public int createExpedition(int companyId, String departureCity, String arrivalCity, String date, String time, int capacity, double price, int duration) {
        int departureCityId = cityRepository.findIdByName(departureCity);
        int arrivalCityId = cityRepository.findIdByName(arrivalCity);

        if(departureCityId == -1 || arrivalCityId == -1) {
            return -1;
        }

        Instant instantDate = Instant.parse(date + "T" + time + ":00Z");

        Expedition expedition = new Expedition(
            departureCityId,
            arrivalCityId,
            instantDate,
            BigDecimal.valueOf(price),
            duration,
            capacity,
            companyId
        );

        expeditionRepository.save(expedition);

        return expedition.getId();
    }

    public List<ExpeditionForCustomerDTO> findExpeditionsByInstantAndRoute(String departureCity, String arrivalCity, String date) {
        int departureCityId = cityRepository.findIdByName(departureCity);
        int arrivalCityId = cityRepository.findIdByName(arrivalCity);

        if(departureCityId == -1 || arrivalCityId == -1) {
            return List.of();
        }

        Instant instantDate = Instant.parse(date + "T00:00:00Z");
        Instant endOfDay = instantDate.plusSeconds(86399); // Add 23 hours, 59 minutes, and 59 seconds to get the end of the day
        
        return DTOMapperUtils.toExpeditionForCustomerDTO(
            expeditionRepository.findByInstantAndRoute(
                departureCityId, 
                arrivalCityId, 
                instantDate, 
                endOfDay
            )
        );
    }

    public List<ExpeditionForCompanyDTO> findExpeditionsByInstantAndCompanyId(String date, int companyId) {
        Instant instantDate = Instant.parse(date + "T00:00:00Z");
        Instant endOfDay = instantDate.plusSeconds(86399); // Add 23 hours, 59 minutes, and 59 seconds to get the end of the day
        
        return DTOMapperUtils.toExpeditionForCompanyDTO(
            expeditionRepository.findAllByInstantAndCompanyId(
                instantDate, 
                endOfDay,
                companyId
            )
        );
    }

    public List<ExpeditionForCompanyDTO> findUpcomingExpeditions(int companyId) {
        Instant now = Instant.now();
        return DTOMapperUtils.toExpeditionForCompanyDTO(
            expeditionRepository.findUpcomingExpeditions(
                companyId, 
                now
            )
        );
    }

    public List<ExpeditionForCompanyDTO> findAllExpeditions(int companyId) {
        return DTOMapperUtils.toExpeditionForCompanyDTO(
            expeditionRepository.findAllByCompanyId(
                companyId
            )
        );
    }

    public boolean expeditionExists(int expeditionId) {
        return expeditionRepository.existsById(expeditionId);
    }

    public boolean bookSeat(int expeditionId) {
        Expedition expedition = expeditionRepository.findById(expeditionId).orElse(null);

        if(expedition == null) {
            return false;
        }

        expedition.setNumberOfBookedSeats(expedition.getNumberOfBookedSeats() + 1);
        expedition.setProfit(expedition.getProfit().add(expedition.getPrice()));
        expeditionRepository.save(expedition);
        
        System.out.println("Expedition after booking: " + expedition.toString());
        return true;
    }

    public ExpeditionStatus canBeReserved(int expeditionId) {
        Expedition expedition = expeditionRepository.findById(expeditionId).orElse(null);

        if(expedition == null) {
            return ExpeditionStatus.NOT_FOUND;
        }

        if(expedition.getCapacity() <= 0) {
            return ExpeditionStatus.NOT_VALID;
        }

        if(expeditionRepository.isExpeditionTimePassed(expeditionId, Instant.now())) {
            return ExpeditionStatus.INVALID_TIME;
        }

        if(expedition.getCapacity() <= expedition.getNumberOfBookedSeats()) {
            return ExpeditionStatus.ALREADY_BOOKED;
        }

        return ExpeditionStatus.SUCCESS;
    }
    
    public int getExpeditionPrice(int expeditionId) {
        Expedition expedition = expeditionRepository.findById(expeditionId).orElse(null);

        if(expedition == null) {
            return -1; // or throw an exception
        }

        return expedition.getPrice().intValue();
    }
}

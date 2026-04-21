package com.shubilet.api_gateway.mappers;

import com.shubilet.api_gateway.dataTransferObjects.external.responses.expeditionOperations.SeatForCompanyExternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.expeditionOperations.SeatsForCompanyInternalDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.expeditionOperations.CustomerIdNameMapDTO;
import com.shubilet.api_gateway.dataTransferObjects.internal.responses.expeditionOperations.SeatForCompanyInternalDTO;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CustomerIdNameMapper {

    public CustomerIdNameMapper() {
        throw new UnsupportedOperationException("Mapper class cannot be instantiated.");
    }

    public static List<SeatForCompanyExternalDTO> toSeatsForCompanyExternalDTO(SeatsForCompanyInternalDTO seatsForCompanyInternalDTO, CustomerIdNameMapDTO customerIdNameMapDTO) {
        List<SeatForCompanyExternalDTO> matchedSeats = new LinkedList<>();
        HashMap<Integer, String> customerMap = customerIdNameMapDTO.getCustomers();

        for (SeatForCompanyInternalDTO seat : seatsForCompanyInternalDTO.getSeats()) {
            matchedSeats.add(new SeatForCompanyExternalDTO(
                    seat.getSeatId(),
                    seat.getExpeditionId(),
                    seat.getSeatNo(),
                    customerMap.get(seat.getCustomerId()),
                    seat.getStatus()));
        }
        return matchedSeats;
    }
}

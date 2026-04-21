package com.shubilet.expedition_service.dataTransferObjects.responses.forRepositories;

import com.shubilet.expedition_service.common.enums.SeatStatusForModel;

public class SeatForCustomerRepoDTO {
    private Integer expeditionId;
    private Integer seatNo;
    private SeatStatusForModel status;

    public SeatForCustomerRepoDTO(
        Integer expeditionId,
        Integer seatNo,
        SeatStatusForModel status
    ) {
        this.expeditionId = expeditionId;
        this.seatNo = seatNo;
        this.status = status;
    }

    public Integer getExpeditionId() {
        return expeditionId;
    }
    public void setExpeditionId(Integer expeditionId) {
        this.expeditionId = expeditionId;
    }

    public Integer getSeatNo() {
        return seatNo;
    }
    public void setSeatNo(Integer seatNo) {
        this.seatNo = seatNo;
    }

    public SeatStatusForModel getStatus() {
        return status;
    }
    public void setStatus(SeatStatusForModel status) {
        this.status = status;
    }
}

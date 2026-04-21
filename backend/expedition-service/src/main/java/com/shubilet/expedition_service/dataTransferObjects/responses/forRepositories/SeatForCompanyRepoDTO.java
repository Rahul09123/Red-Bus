package com.shubilet.expedition_service.dataTransferObjects.responses.forRepositories;

import com.shubilet.expedition_service.common.enums.SeatStatusForModel;

public class SeatForCompanyRepoDTO {
    private Integer seatId;
    private Integer expeditionId;
    private Integer seatNo;
    private Integer customerId;
    private SeatStatusForModel status;

    public SeatForCompanyRepoDTO(
        Integer seatId, 
        Integer expeditionId, 
        Integer seatNo, 
        Integer customerId, 
        SeatStatusForModel status
    ) {
        this.seatId = seatId;
        this.expeditionId = expeditionId;
        this.seatNo = seatNo;
        this.customerId = customerId;
        this.status = status;
    }

    public Integer getSeatId() {
        return seatId;
    }
    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
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

    public Integer getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public SeatStatusForModel getStatus() {
        return status;
    }
    public void setStatus(SeatStatusForModel status) {
        this.status = status;
    }
}

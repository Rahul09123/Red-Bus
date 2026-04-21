package com.shubilet.expedition_service.dataTransferObjects.responses.base;

public class SeatForCompanyDTO {
    private int seatId;
    private int expeditionId;
    private int seatNo;
    private int customerId;
    private String status;

    public SeatForCompanyDTO() {

    }

    public SeatForCompanyDTO(
        int seatId,
        int expeditionId,
        int seatNo,
        int customerId,
        String status
    ) {
        this.seatId = seatId;
        this.expeditionId = expeditionId;
        this.seatNo = seatNo;
        this.customerId = customerId;
        this.status = status;
    }

    public int getSeatId() {
        return seatId;
    }
    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public int getExpeditionId() {
        return expeditionId;
    }
    public void setExpeditionId(int expeditionId) {
        this.expeditionId = expeditionId;
    }

    public int getSeatNo() {
        return seatNo;
    }
    public void setSeatNo(int seatNo) {
        this.seatNo = seatNo;
    }

    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}

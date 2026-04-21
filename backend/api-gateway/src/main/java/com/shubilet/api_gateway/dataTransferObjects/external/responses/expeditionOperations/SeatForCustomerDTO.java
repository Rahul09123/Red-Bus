package com.shubilet.api_gateway.dataTransferObjects.external.responses.expeditionOperations;

public class SeatForCustomerDTO {
    private int expeditionId;
    private int seatNo;
    private String status;

    public SeatForCustomerDTO() {

    }

    public SeatForCustomerDTO(
        int expeditionId, 
        int seatNo, 
        String status
    ) {
        this.expeditionId = expeditionId;
        this.seatNo = seatNo;
        this.status = status;
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

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
}

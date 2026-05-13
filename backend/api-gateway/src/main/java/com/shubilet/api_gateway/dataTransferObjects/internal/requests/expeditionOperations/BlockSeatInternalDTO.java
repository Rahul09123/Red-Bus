package com.shubilet.api_gateway.dataTransferObjects.internal.requests.expeditionOperations;

public class BlockSeatInternalDTO {
    private int expeditionId;
    private int seatNo;
    private int customerId;

    public BlockSeatInternalDTO() {
    }

    public BlockSeatInternalDTO(int expeditionId, int seatNo, int customerId) {
        this.expeditionId = expeditionId;
        this.seatNo = seatNo;
        this.customerId = customerId;
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
}

package com.shubilet.api_gateway.dataTransferObjects.external.requests.expeditionOperations;

public class BlockSeatExternalDTO {
    private int expeditionId;
    private int seatNo;

    public BlockSeatExternalDTO() {
    }

    public BlockSeatExternalDTO(int expeditionId, int seatNo) {
        this.expeditionId = expeditionId;
        this.seatNo = seatNo;
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
}

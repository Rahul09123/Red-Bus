package com.shubilet.api_gateway.dataTransferObjects.external.responses.expeditionOperations;

public class SeatForCompanyExternalDTO {
    private int seatId;
    private int expeditionId;
    private int seatNo;
    private String customerFullName;
    private String status;

    public SeatForCompanyExternalDTO() {

    }

    public SeatForCompanyExternalDTO(
        int seatId,
        int expeditionId,
        int seatNo,
        String customerFullName,
        String status
    ) {
        this.seatId = seatId;
        this.expeditionId = expeditionId;
        this.seatNo = seatNo;
        this.customerFullName = customerFullName;
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

    public String getCustomerFullName() {
        return customerFullName;
    }
    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}

package com.shubilet.expedition_service.dataTransferObjects.requests;

public class ExpeditionViewByDateDTO {
    private int companyId;
    private String date;

    public ExpeditionViewByDateDTO() {

    }

    public ExpeditionViewByDateDTO(int companyId, String date) {
        this.companyId = companyId;
        this.date = date;
    }

    public int getCompanyId() {
        return companyId;
    }
    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}

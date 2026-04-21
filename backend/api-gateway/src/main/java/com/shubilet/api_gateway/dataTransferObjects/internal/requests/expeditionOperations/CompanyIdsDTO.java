package com.shubilet.api_gateway.dataTransferObjects.internal.requests.expeditionOperations;

import com.shubilet.api_gateway.dataTransferObjects.internal.requests.CompanyIdDTO;

import java.util.List;

public class CompanyIdsDTO {
    private List<CompanyIdDTO> companyIds;

    public CompanyIdsDTO(List<CompanyIdDTO> companyIds) {
        this.companyIds = companyIds;
    }

    public List<CompanyIdDTO> getCompanyIds() {
        return companyIds;
    }

    public void setCompanyIds(List<CompanyIdDTO> companyIds) {
        this.companyIds = companyIds;
    }
}

package com.shubilet.expedition_service.dataTransferObjects.responses.complex;

import com.shubilet.expedition_service.dataTransferObjects.responses.base.CompanyStatsDTO;

public class CompanyStatsResponseDTO {
    private String message;
    private CompanyStatsDTO stats;

    public CompanyStatsResponseDTO() {}

    public CompanyStatsResponseDTO(String message, CompanyStatsDTO stats) {
        this.message = message;
        this.stats = stats;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public CompanyStatsDTO getStats() { return stats; }
    public void setStats(CompanyStatsDTO stats) { this.stats = stats; }
}

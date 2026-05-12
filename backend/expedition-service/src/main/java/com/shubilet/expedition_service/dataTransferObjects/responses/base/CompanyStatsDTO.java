package com.shubilet.expedition_service.dataTransferObjects.responses.base;

public class CompanyStatsDTO {
    private long totalExpeditions;
    private long totalBookedSeats;
    private double totalProfit;
    private long activeExpeditions;

    public CompanyStatsDTO() {}

    public CompanyStatsDTO(long totalExpeditions, long totalBookedSeats, double totalProfit, long activeExpeditions) {
        this.totalExpeditions = totalExpeditions;
        this.totalBookedSeats = totalBookedSeats;
        this.totalProfit = totalProfit;
        this.activeExpeditions = activeExpeditions;
    }

    // Getters and Setters
    public long getTotalExpeditions() { return totalExpeditions; }
    public void setTotalExpeditions(long totalExpeditions) { this.totalExpeditions = totalExpeditions; }

    public long getTotalBookedSeats() { return totalBookedSeats; }
    public void setTotalBookedSeats(long totalBookedSeats) { this.totalBookedSeats = totalBookedSeats; }

    public double getTotalProfit() { return totalProfit; }
    public void setTotalProfit(double totalProfit) { this.totalProfit = totalProfit; }

    public long getActiveExpeditions() { return activeExpeditions; }
    public void setActiveExpeditions(long activeExpeditions) { this.activeExpeditions = activeExpeditions; }
}

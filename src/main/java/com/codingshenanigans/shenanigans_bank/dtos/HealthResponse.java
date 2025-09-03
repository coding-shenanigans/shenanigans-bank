package com.codingshenanigans.shenanigans_bank.dtos;

public class HealthResponse {
    private String status;

    public HealthResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package com.gl.project.entities;

public enum OrderStatus {
    INPROGRESS ("inProgress"),
    COMPLETED ("completed");

    private String status;
    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

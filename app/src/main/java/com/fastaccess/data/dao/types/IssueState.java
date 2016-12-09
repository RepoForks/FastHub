package com.fastaccess.data.dao.types;

public enum IssueState {
    open("Opened"),
    closed("Closed");

    private String status;

    IssueState(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
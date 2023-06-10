package com.strelnikov.issuetracker.entity;

public enum IssuePriority {
    LOW("L"),
    MEDIUM("M"),
    HIGH("H");

    private String code;

    IssuePriority(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

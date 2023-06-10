package com.strelnikov.issuetracker.entity;

public enum IssueType {
    TASK("T"),
    BUG("B"),
    MILESTONE("M");

    private String code;

    IssueType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

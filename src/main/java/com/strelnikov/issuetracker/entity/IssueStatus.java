package com.strelnikov.issuetracker.entity;

public enum IssueStatus {
    TODO("O"),
    INPROGRESS("P"),
    INREVIEW("R"),
    DONE("D");

    private String code;

    IssueStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

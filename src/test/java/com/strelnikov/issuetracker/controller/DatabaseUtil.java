package com.strelnikov.issuetracker.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;

public class DatabaseUtil {
    private static final List<String> TABLES = List.of(
            "users",
            "projects",
            "issues",
            "projects_roles",
            "issues_roles",
            "tags",
            "issues_tags",
            "users_roles"
    );

    public static void cleanDatabase(JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, TABLES.toArray(String[]::new));
    }
}

package com.strelnikov.issuetracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StatusController {

    @GetMapping("/status")
    public String all() {
        return "to implement";
    }

    @GetMapping("/status/{id}")
    public String getStatus(@PathVariable String id) {
        return "to implement";
    }
}

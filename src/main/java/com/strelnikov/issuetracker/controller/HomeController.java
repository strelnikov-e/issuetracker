package com.strelnikov.issuetracker.controller;

import com.strelnikov.issuetracker.config.RestEndpointsProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    private final RestEndpointsProperties endpointsProperties;

    public HomeController(RestEndpointsProperties endpointsProperties) {
        this.endpointsProperties = endpointsProperties;
    }

    @GetMapping
    public String welcome() {
        return "Welcome to Issue tracker!<br><a href=/swagger-ui/index.html#>REST API endpoints</a>";
    }

    @GetMapping("/api")
    public String endpoints() {
        return endpointsProperties.publicEndpoints();
    }
}

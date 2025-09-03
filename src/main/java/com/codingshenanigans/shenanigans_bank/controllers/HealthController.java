package com.codingshenanigans.shenanigans_bank.controllers;

import com.codingshenanigans.shenanigans_bank.dtos.HealthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        HealthResponse response = new HealthResponse("OK");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

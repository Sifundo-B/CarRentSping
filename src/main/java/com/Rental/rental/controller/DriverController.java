package com.Rental.rental.controller;

import com.Rental.rental.entity.Driver;
import com.Rental.rental.service.DriverService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addDriver(@RequestParam("driver") String driverDetails, @RequestParam("licenseFile") MultipartFile licenseFile) {
        try {
            Driver driver = new ObjectMapper().readValue(driverDetails, Driver.class);
            Driver savedDriver = driverService.addDriver(driver, licenseFile);
            return ResponseEntity.ok(savedDriver);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading license or saving driver");
        }
    }

    @GetMapping("/{driverId}")
    public ResponseEntity<Driver> getDriverById(@PathVariable Long driverId) {
        return driverService.getDriverById(driverId)
                .map(driver -> ResponseEntity.ok(driver))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}


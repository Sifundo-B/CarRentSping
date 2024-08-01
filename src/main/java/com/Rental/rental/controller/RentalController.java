package com.Rental.rental.controller;

import com.Rental.rental.entity.Rental;
import com.Rental.rental.entity.User;
import com.Rental.rental.service.RentalService;
import com.Rental.rental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;
    @Autowired
    private UserService userService;

    @PostMapping("/rent")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> rentCar(
            @RequestParam Long carId,
            @RequestParam Long userId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) Long driverId,
            @RequestParam String pickUpLocation,
            @RequestParam String dropOffLocation) {

        try {
            Rental rental = rentalService.rentCar(carId, userId, startDate, endDate, driverId, pickUpLocation, dropOffLocation);
            return ResponseEntity.ok(rental);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public List<Rental> getRentalsByUser(@PathVariable Long userId) {
        User user = userService.findByEmail(userId.toString());
        return rentalService.getRentalsByUser(user);
    }
}


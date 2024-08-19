package com.Rental.rental.controller;

import com.Rental.rental.dto.RentalRequest;
import com.Rental.rental.dto.RentalResponse;
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
    public ResponseEntity<?> rentCar(@RequestBody RentalRequest rentalRequest) {
        try {
            Rental rental = rentalService.rentCar(
                    rentalRequest.getCarId(),
                    rentalRequest.getUserId(),
                    rentalRequest.getStartDate(),
                    rentalRequest.getEndDate(),
                    rentalRequest.getDriverId(),
                    rentalRequest.getPickUpLocation(),
                    rentalRequest.getDropOffLocation()
            );

            RentalResponse rentalResponse = new RentalResponse();
            rentalResponse.setRentalId(rental.getId());
            rentalResponse.setCarId(rental.getCar().getId());
            rentalResponse.setUserId(rental.getUser().getId());
            rentalResponse.setDriverId(rental.getDriver() != null ? rental.getDriver().getId() : null);
            rentalResponse.setPickUpLocation(rental.getPickUpLocation());
            rentalResponse.setDropOffLocation(rental.getDropOffLocation());
            rentalResponse.setStartDate(rental.getRentalStartDate().toString());
            rentalResponse.setEndDate(rental.getRentalEndDate().toString());
            rentalResponse.setTotalPrice(rental.getTotalPrice().toString());

            return ResponseEntity.ok(rentalResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RentalResponse>> getRentalsByUser(@PathVariable Long userId) {
        User user = userService.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<RentalResponse> rentals = rentalService.getRentalsByUser(user);
        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/{rentalId}")
    public ResponseEntity<RentalResponse> getRentalById(@PathVariable Long rentalId) {
        try {
            Rental rental = rentalService.getRentalById(rentalId);
            if (rental == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            RentalResponse response = rentalService.convertToRentalResponse(rental);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
//            logger.error("Failed to retrieve rental with ID " + rentalId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}


package com.Rental.rental.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class RentalRequest {
    private Long carId;
    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long driverId;
    private String pickUpLocation;
    private String dropOffLocation;

}
package com.Rental.rental.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalResponse {
    private Long rentalId;
    private Long carId;
    private Long userId;
    private Long driverId;
    private String pickUpLocation;
    private String dropOffLocation;
    private String startDate;
    private String endDate;
    private String totalPrice;

    // User details
    private String userFirstName;
    private String userLastName;
    private String userEmail;

    // Car details
    private String carMake;
    private String carModel;
    private String carType;
    private String carRentalPrice;
}

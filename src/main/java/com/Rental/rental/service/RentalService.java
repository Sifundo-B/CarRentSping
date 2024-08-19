package com.Rental.rental.service;

import com.Rental.rental.dto.RentalResponse;
import com.Rental.rental.entity.Rental;
import com.Rental.rental.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface RentalService {
    Rental rentCar(Long carId, Long userId, LocalDate startDate, LocalDate endDate, Long driverId, String pickUpLocation, String dropOffLocation);
    List<RentalResponse> getRentalsByUser(User user);
    Rental getRentalById(Long rentalId);
    RentalResponse convertToRentalResponse(Rental rental);
}

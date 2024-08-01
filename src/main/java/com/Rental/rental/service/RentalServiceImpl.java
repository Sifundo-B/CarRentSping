package com.Rental.rental.service;

import com.Rental.rental.entity.Car;
import com.Rental.rental.entity.Driver;
import com.Rental.rental.entity.Rental;
import com.Rental.rental.entity.User;
import com.Rental.rental.exceptions.*;
import com.Rental.rental.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RentalServiceImpl implements RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CarService carService;

    @Autowired
    private UserService userService;

    @Autowired
    private DriverService driverService;

    @Override
    public Rental rentCar(Long carId, Long userId, LocalDate startDate, LocalDate endDate, Long driverId, String pickUpLocation, String dropOffLocation) {
        // Validate rental period
        validateRentalPeriod(startDate, endDate);

        // Fetch and validate car
        Car car = carService.getCarById(carId)
                .orElseThrow(() -> new CarNotFoundException("Car with ID " + carId + " not found"));

        if (!car.isAvailability()) {
            throw new CarNotAvailableException("Car with ID " + carId + " is not available for rental");
        }

        // Fetch and validate user
        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));

        // Fetch and validate driver if provided
        Driver driver = null;
        if (driverId != null) {
            driver = driverService.getDriverById(driverId)
                    .orElseThrow(() -> new DriverNotFoundException("Driver with ID " + driverId + " not found"));
        }

        // Create and save rental
        Rental rental = new Rental();
        rental.setUser(user);
        rental.setCar(car);
        rental.setDriver(driver);
        rental.setRentalStartDate(startDate);
        rental.setRentalEndDate(endDate);
        rental.setPickUpLocation(pickUpLocation);
        rental.setDropOffLocation(dropOffLocation);
        rental.setTotalPrice(calculateTotalPrice(car.getRentalPrice(), startDate, endDate, driver));

        // Mark car as unavailable
        carService.updateCarAvailability(carId, false);

        return rentalRepository.save(rental);
    }

    private void validateRentalPeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidRentalPeriodException("The rental start date cannot be after the end date");
        }

        if (startDate.isBefore(LocalDate.now())) {
            throw new InvalidRentalPeriodException("The rental start date cannot be in the past");
        }
    }

    private BigDecimal calculateTotalPrice(BigDecimal rentalPrice, LocalDate startDate, LocalDate endDate, Driver driver) {
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        BigDecimal totalPrice = rentalPrice.multiply(BigDecimal.valueOf(days));
        if (driver != null) {
            totalPrice = totalPrice.add(new BigDecimal("50.00"));
        }
        return totalPrice;
    }

    @Override
    public List<Rental> getRentalsByUser(User user) {
        return rentalRepository.findByUser(user);
    }
}

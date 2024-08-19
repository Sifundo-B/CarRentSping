package com.Rental.rental.service;

import com.Rental.rental.dto.RentalResponse;
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
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Rental getRentalById(Long rentalId) {
        Optional<Rental> rental = rentalRepository.findById(rentalId);
        if (rental.isPresent()) {
            Rental rentalEntity = rental.get();
            // Force loading of related entities
            rentalEntity.getUser().getFirstName(); // Access user details to force load
            rentalEntity.getUser().getEmail();
            rentalEntity.getCar().getMake(); // Access car details to force load
            rentalEntity.getCar().getModel();
            rentalEntity.getCar().getType();
            rentalEntity.getCar().getRentalPrice();
            return rentalEntity;
        }
        return null; // Return null if rental not found
    }
    @Override
    public RentalResponse convertToRentalResponse(Rental rental) {
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

        // Populate user details
        User user = rental.getUser();
        rentalResponse.setUserFirstName(user.getFirstName());
        rentalResponse.setUserLastName(user.getLastName());
        rentalResponse.setUserEmail(user.getEmail());

        // Populate car details
        Car car = rental.getCar();
        rentalResponse.setCarMake(car.getMake());
        rentalResponse.setCarModel(car.getModel());
        rentalResponse.setCarType(car.getType());
        rentalResponse.setCarRentalPrice(car.getRentalPrice().toString());

        return rentalResponse;
    }
    @Override
    public List<RentalResponse> getRentalsByUser(User user) {
        List<Rental> rentals = rentalRepository.findByUser(user);
        return rentals.stream()
                .map(this::convertToRentalResponse)
                .collect(Collectors.toList());
    }

}

package com.Rental.rental.service;

import com.Rental.rental.entity.Car;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CarService {
    Car addCar(Car car, MultipartFile file) throws IOException;
    List<Car> getAvailableCars();

    List<Car> getAllCars();

    Optional<Car> getCarById(Long id);
    Car updateCarAvailability(Long carId, boolean availability);
}

package com.Rental.rental.service;

import com.Rental.rental.entity.Car;
import com.Rental.rental.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public Car addCar(Car car, MultipartFile file) throws IOException {
        Map uploadResult = cloudinaryService.upload(file);
        car.setImageUrl(uploadResult.get("url").toString());
        car.setAvailability(true);
        return carRepository.save(car);
    }

    @Override
    public List<Car> getAvailableCars() {
        return carRepository.findByAvailability(true);
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    @Override
    public Car updateCarAvailability(Long carId, boolean availability) {
        Optional<Car> carOptional = carRepository.findById(carId);
        if (carOptional.isPresent()) {
            Car car = carOptional.get();
            car.setAvailability(availability);
            return carRepository.save(car);
        }
        return null;
    }
}

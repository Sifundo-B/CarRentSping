package com.Rental.rental.repository;

import com.Rental.rental.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByAvailability(boolean availability);
}

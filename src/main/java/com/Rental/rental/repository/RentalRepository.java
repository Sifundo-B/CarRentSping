package com.Rental.rental.repository;

import com.Rental.rental.entity.Rental;
import com.Rental.rental.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    List<Rental> findByUser(User user);
}
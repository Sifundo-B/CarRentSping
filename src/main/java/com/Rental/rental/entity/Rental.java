package com.Rental.rental.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = true)
    private Driver driver;
    private String pickUpLocation;
    private String dropOffLocation;

    private LocalDate rentalStartDate;
    private LocalDate rentalEndDate;
    private BigDecimal totalPrice;

}

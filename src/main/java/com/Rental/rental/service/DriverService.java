package com.Rental.rental.service;

import com.Rental.rental.entity.Driver;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface DriverService {
    Driver addDriver(Driver driver, MultipartFile licenseFile) throws IOException;
    Optional<Driver> getDriverById(Long id);
}

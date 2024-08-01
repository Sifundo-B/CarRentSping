package com.Rental.rental.service;

import com.Rental.rental.entity.Driver;
import com.Rental.rental.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public Driver addDriver(Driver driver, MultipartFile licenseFile) throws IOException {
        Map uploadResult = cloudinaryService.upload(licenseFile);
        driver.setLicenseImageUrl(uploadResult.get("url").toString());
        return driverRepository.save(driver);
    }

    @Override
    public Optional<Driver> getDriverById(Long id) {
        return driverRepository.findById(id);
    }
    @Override
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }
}

